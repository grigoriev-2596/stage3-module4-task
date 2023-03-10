package com.mjc.school.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.mjc.school.repository.implementation.TagRepositoryImpl;
import com.mjc.school.repository.model.TagEntity;
import com.mjc.school.repository.query.TagSearchParams;
import com.mjc.school.service.TagService;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import com.mjc.school.service.exceptions.ErrorCode;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.mapper.TagMapper;
import com.mjc.school.service.query.TagSearchCriteriaParams;
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
public class TagServiceImpl implements TagService {

    private final TagRepositoryImpl tagRepository;

    private final TagMapper tagMapper;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .findAndRegisterModules();

    private final Validator springValidator;


    @Autowired
    public TagServiceImpl(TagRepositoryImpl tagRepository, TagMapper tagMapper, Validator springValidator) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
        this.springValidator = springValidator;
    }

    @Transactional(readOnly = true)
    @Override
    public List<TagDtoResponse> getAll() {
        return tagMapper.listOfModelsToListOfResponses(tagRepository.getAll());
    }

    @Transactional(readOnly = true)
    @Override
    public TagDtoResponse getById(Long id) {
        TagEntity entity = tagRepository.getById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorCode.TAG_DOES_NOT_EXIST.toString(), id)));
        return tagMapper.modelToDtoResponse(entity);
    }

    @Transactional
    @Override
    public TagDtoResponse create(TagDtoRequest createRequest) {
        TagEntity createdTag = tagRepository.create(tagMapper.dtoRequestToModel(createRequest));
        return tagMapper.modelToDtoResponse(createdTag);
    }

    @Transactional
    @Override
    public TagDtoResponse update(TagDtoRequest updateRequest) {
        tagExistsOrThrowException(updateRequest.id());
        TagEntity updateModel = tagRepository.update(tagMapper.dtoRequestToModel(updateRequest));
        return tagMapper.modelToDtoResponse(updateModel);
    }

    @Transactional
    @Override
    public boolean deleteById(Long id) {
        tagExistsOrThrowException(id);
        return tagRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TagDtoResponse> getTags(TagSearchCriteriaParams params, Pageable pageable) {
        TagSearchParams searchParams = new TagSearchParams(params.name(), params.newsId());

        Page<TagEntity> tagEntityPage = tagRepository.getTags(searchParams, pageable);
        List<TagDtoResponse> tagDtoResponses = tagMapper.listOfModelsToListOfResponses(tagEntityPage.getContent());
        return new PageImpl<>(tagDtoResponses, pageable, tagEntityPage.getTotalElements());
    }

    @Transactional
    @Override
    public TagDtoResponse patch(Long id, JsonPatch patch) {
        TagDtoResponse response = getById(id);
        TagDtoRequest request = new TagDtoRequest(response.id(), response.name());

        try {
            JsonNode node = patch.apply(objectMapper.convertValue(request, JsonNode.class));
            TagDtoRequest patchedTag = objectMapper.treeToValue(node, TagDtoRequest.class);

            Set<ConstraintViolation<TagDtoRequest>> constraintViolations = springValidator.validate(patchedTag);
            if (!constraintViolations.isEmpty()) {
                throw new ConstraintViolationException(constraintViolations);
            }

            return update(patchedTag);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new RuntimeException(e.getClass() + ": " + e.getMessage());
        }
    }

    private void tagExistsOrThrowException(Long id) {
        if (!tagRepository.existById(id)) {
            throw new NotFoundException(String.format(ErrorCode.TAG_DOES_NOT_EXIST.toString(), id));
        }
    }
}
