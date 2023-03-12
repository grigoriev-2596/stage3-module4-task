package com.mjc.school.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.mjc.school.repository.implementation.AuthorRepositoryImpl;
import com.mjc.school.repository.implementation.NewsRepositoryImpl;
import com.mjc.school.repository.implementation.TagRepositoryImpl;
import com.mjc.school.repository.model.NewsEntity;
import com.mjc.school.repository.query.NewsSearchParams;
import com.mjc.school.service.NewsService;
import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;
import com.mjc.school.service.exceptions.ErrorCode;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.mapper.NewsMapper;
import com.mjc.school.service.query.NewsSearchCriteriaParams;
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
public class NewsServiceImpl implements NewsService {

    private final NewsRepositoryImpl newsRepository;
    private final AuthorRepositoryImpl authorRepository;
    private final TagRepositoryImpl tagRepository;

    private final NewsMapper newsMapper;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .findAndRegisterModules();

    private final Validator springValidator;

    @Autowired
    public NewsServiceImpl(NewsRepositoryImpl newsRepository, AuthorRepositoryImpl authorRepository,
                           TagRepositoryImpl tagRepository, NewsMapper newsMapper, Validator springValidator) {
        this.newsRepository = newsRepository;
        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
        this.newsMapper = newsMapper;
        this.springValidator = springValidator;
    }

    @Transactional(readOnly = true)
    @Override
    public List<NewsDtoResponse> getAll() {
        return newsMapper.listOfModelsToListOfResponses(newsRepository.getAll());
    }

    @Transactional(readOnly = true)
    @Override
    public NewsDtoResponse getById(Long id) {
        NewsEntity entity = newsRepository.getById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ErrorCode.NEWS_DOES_NOT_EXIST.toString(), id)));

        return newsMapper.modelToDtoResponse(entity);
    }

    @Transactional
    @Override
    public NewsDtoResponse create(NewsDtoRequest createRequest) {
        authorExistsOrThrowException(createRequest.authorId());
        tagsExistOrThrowException(createRequest.tagIds());

        NewsEntity createdNews = newsRepository.create(newsMapper.dtoRequestToModel(createRequest));
        return newsMapper.modelToDtoResponse(createdNews);
    }

    @Transactional
    @Override
    public NewsDtoResponse update(NewsDtoRequest updateRequest) {
        newsExistsOrThrowException(updateRequest.id());
        authorExistsOrThrowException(updateRequest.authorId());
        tagsExistOrThrowException(updateRequest.tagIds());

        NewsEntity updatedNews = newsRepository.update(newsMapper.dtoRequestToModel(updateRequest));
        return newsMapper.modelToDtoResponse(updatedNews);
    }

    @Transactional
    @Override
    public boolean deleteById(Long id) {
        newsExistsOrThrowException(id);
        return newsRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<NewsDtoResponse> getNews(Pageable pageable, NewsSearchCriteriaParams params) {
        validateConstraintsOrThrowException(params);
        NewsSearchParams searchParams = new NewsSearchParams(
                params.title(),
                params.content(),
                params.authorName(),
                params.tagNames(),
                params.tagIds());
        Page<NewsEntity> newsEntityPage = newsRepository.getNews(searchParams, pageable);
        List<NewsDtoResponse> newsDtoResponses = newsMapper.listOfModelsToListOfResponses(newsEntityPage.getContent());
        return new PageImpl<>(newsDtoResponses, pageable, newsEntityPage.getTotalElements());
    }

    @Transactional
    @Override
    public NewsDtoResponse patch(Long id, JsonPatch patch) {
        NewsDtoResponse response = getById(id);
        NewsDtoRequest request = new NewsDtoRequest(response.id(), response.title(), response.content(),
                response.author().id(), response.tags().stream().map(t -> t.id()).toList());
        try {
            JsonNode node = patch.apply(objectMapper.convertValue(request, JsonNode.class));
            NewsDtoRequest patchedNews = objectMapper.treeToValue(node, NewsDtoRequest.class);
            validateConstraintsOrThrowException(patchedNews);

            return update(patchedNews);

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

    private void newsExistsOrThrowException(Long id) {
        if (!newsRepository.existById(id)) {
            throw new NotFoundException(String.format(ErrorCode.NEWS_DOES_NOT_EXIST.toString(), id));
        }
    }

    private void tagsExistOrThrowException(List<Long> ids) {
        ids.forEach(id -> {
            if (!tagRepository.existById(id)) {
                throw new NotFoundException(String.format(ErrorCode.TAG_DOES_NOT_EXIST.toString(), id));
            }
        });
    }


}
