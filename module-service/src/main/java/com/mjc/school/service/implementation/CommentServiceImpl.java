package com.mjc.school.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.mjc.school.repository.implementation.CommentRepositoryImpl;
import com.mjc.school.repository.implementation.NewsRepositoryImpl;
import com.mjc.school.repository.model.CommentEntity;
import com.mjc.school.repository.query.CommentSearchParams;
import com.mjc.school.service.CommentService;
import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.CommentDtoResponse;
import com.mjc.school.service.exceptions.ErrorCode;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.mapper.CommentMapper;
import com.mjc.school.service.query.CommentSearchCriteriaParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepositoryImpl commentRepository;
    private final NewsRepositoryImpl newsRepository;

    private final CommentMapper commentMapper;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .findAndRegisterModules();

    private final Validator springValidator;

    @Autowired
    public CommentServiceImpl(CommentRepositoryImpl commentRepository, NewsRepositoryImpl newsRepository,
                              CommentMapper commentMapper, Validator springValidator) {
        this.commentRepository = commentRepository;
        this.newsRepository = newsRepository;
        this.commentMapper = commentMapper;
        this.springValidator = springValidator;
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDtoResponse> getAll() {
        return commentMapper.listOfModelsToListOfResponses(commentRepository.getAll());
    }

    @Transactional(readOnly = true)
    @Override
    public CommentDtoResponse getById(Long id) {
        CommentEntity entity = commentRepository.getById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorCode.COMMENT_DOES_NOT_EXIST.toString(), id)));

        return commentMapper.modelToDtoResponse(entity);
    }

    @Transactional
    @Override
    public CommentDtoResponse create(CommentDtoRequest createRequest) {
        newsExistOrThrowException(createRequest.newsId());
        CommentEntity createdComment = commentRepository.create(commentMapper.dtoRequestToModel(createRequest));

        return commentMapper.modelToDtoResponse(createdComment);
    }

    @Transactional
    @Override
    public CommentDtoResponse update(CommentDtoRequest updateRequest) {
        commentExistOrThrowException(updateRequest.id());
        newsExistOrThrowException(updateRequest.newsId());

        CommentEntity updatedComment = commentRepository.update(commentMapper.dtoRequestToModel(updateRequest));
        return commentMapper.modelToDtoResponse(updatedComment);
    }

    @Transactional
    @Override
    public boolean deleteById(Long id) {
        commentExistOrThrowException(id);
        return commentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CommentDtoResponse> getComments(Pageable pageable, CommentSearchCriteriaParams params) {
        validateConstraintsOrThrowException(params);
        CommentSearchParams searchParams = new CommentSearchParams(params.content(), params.newsId());

        Page<CommentEntity> commentEntityPage = commentRepository.getComments(searchParams, pageable);
        List<CommentDtoResponse> commentDtoResponses = commentMapper.listOfModelsToListOfResponses(commentEntityPage.getContent());
        return new PageImpl<>(commentDtoResponses, pageable, commentEntityPage.getTotalElements());
    }

    @Transactional
    @Override
    public CommentDtoResponse patch(Long id, JsonPatch patch) {
        CommentDtoResponse response = getById(id);
        CommentDtoRequest request = new CommentDtoRequest(response.id(), response.content(), response.newsId());

        try {
            JsonNode node = patch.apply(objectMapper.convertValue(request, JsonNode.class));
            CommentDtoRequest patchedComment = objectMapper.treeToValue(node, CommentDtoRequest.class);
            validateConstraintsOrThrowException(patchedComment);

            return update(patchedComment);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new RuntimeException(e.getClass() + ": " + e.getMessage());
        }
    }

    private <T> void validateConstraintsOrThrowException(T object) {
        Set<ConstraintViolation<T>> constraintViolations = springValidator.validate(object);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    private void commentExistOrThrowException(Long id) {
        if (!commentRepository.existById(id)) {
            throw new NotFoundException(String.format(ErrorCode.COMMENT_DOES_NOT_EXIST.toString(), id));
        }
    }

    private void newsExistOrThrowException(Long id) {
        if (!newsRepository.existById(id)) {
            throw new NotFoundException(String.format(ErrorCode.NEWS_DOES_NOT_EXIST.toString(), id));
        }
    }
}
