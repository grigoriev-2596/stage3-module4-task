package com.mjc.school.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.mjc.school.repository.implementation.AuthorRepositoryImpl;
import com.mjc.school.repository.model.AuthorEntity;
import com.mjc.school.repository.query.AuthorSearchParams;
import com.mjc.school.service.AuthorService;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.exceptions.ErrorCode;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.mapper.AuthorMapper;
import com.mjc.school.service.query.AuthorSearchCriteriaParams;
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
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepositoryImpl authorRepository;

    private final AuthorMapper authorMapper;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .findAndRegisterModules();

    private final Validator springValidator;

    @Autowired
    public AuthorServiceImpl(AuthorRepositoryImpl authorRepository, AuthorMapper authorMapper, Validator hibernateValidator) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
        this.springValidator = hibernateValidator;
    }

    @Transactional(readOnly = true)
    @Override
    public List<AuthorDtoResponse> getAll() {
        return authorMapper.listOfModelsToListOfResponses(authorRepository.getAll());
    }

    @Transactional(readOnly = true)
    @Override
    public AuthorDtoResponse getById(Long id) {
        AuthorEntity entity = authorRepository.getById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorCode.AUTHOR_DOES_NOT_EXIST.toString(), id)));

        return authorMapper.modelToDtoResponse(entity);
    }

    @Transactional
    @Override
    public AuthorDtoResponse create(AuthorDtoRequest createRequest) {
        AuthorEntity createdAuthor = authorRepository.create(authorMapper.dtoRequestToModel(createRequest));
        return authorMapper.modelToDtoResponse(createdAuthor);
    }

    @Transactional
    @Override
    public AuthorDtoResponse update(AuthorDtoRequest updateRequest) {
        authorExistsOrThrowException(updateRequest.id());
        AuthorEntity updatedAuthor = authorRepository.update(authorMapper.dtoRequestToModel(updateRequest));
        return authorMapper.modelToDtoResponse(updatedAuthor);
    }

    @Transactional
    @Override
    public boolean deleteById(Long id) {
        authorExistsOrThrowException(id);
        return authorRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AuthorDtoResponse> getAuthors(Pageable pageable, AuthorSearchCriteriaParams params) {
        validateConstraintsOrThrowException(params);
        AuthorSearchParams searchParams = new AuthorSearchParams(params.name(), params.newsId());

        Page<AuthorEntity> filteredAuthors = authorRepository.getAuthors(searchParams, pageable);
        List<AuthorDtoResponse> authorDtoResponses = authorMapper.listOfModelsToListOfResponses(filteredAuthors.getContent());
        return new PageImpl<>(authorDtoResponses, pageable, filteredAuthors.getTotalElements());
    }

    @Transactional
    @Override
    public AuthorDtoResponse patch(Long id, JsonPatch patch) {
        AuthorDtoResponse response = getById(id);
        AuthorDtoRequest request = new AuthorDtoRequest(response.id(), response.name());

        try {
            JsonNode node = patch.apply(objectMapper.convertValue(request, JsonNode.class));
            AuthorDtoRequest patchedAuthor = objectMapper.treeToValue(node, AuthorDtoRequest.class);
            validateConstraintsOrThrowException(patchedAuthor);

            return update(patchedAuthor);
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

    private void authorExistsOrThrowException(Long id) {
        if (!authorRepository.existById(id)) {
            throw new NotFoundException(String.format(ErrorCode.AUTHOR_DOES_NOT_EXIST.toString(), id));
        }
    }
}
