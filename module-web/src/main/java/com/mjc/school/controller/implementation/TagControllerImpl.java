package com.mjc.school.controller.implementation;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.controller.TagController;
import com.mjc.school.service.TagService;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import com.mjc.school.service.query.TagSearchCriteriaParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/tags")
public class TagControllerImpl implements TagController {

    private final TagService tagService;

    @Autowired
    public TagControllerImpl(TagService tagService) {
        this.tagService = tagService;
    }

    @Override
    public List<TagDtoResponse> getTags(Pageable pageable, String name, Long newsId) {
        TagSearchCriteriaParams params = new TagSearchCriteriaParams(name, newsId);
        return tagService.getTags(params, pageable).getContent();
    }

    @Override
    public TagDtoResponse getById(Long id) {
        return tagService.getById(id);
    }

    @Override
    public TagDtoResponse create(TagDtoRequest createRequest) {
        return tagService.create(createRequest);
    }

    @Override
    public TagDtoResponse update(Long id, TagDtoRequest updateRequest) {
        return tagService.update(updateRequest);
    }

    @Override
    public Boolean deleteById(Long id) {
        return tagService.deleteById(id);
    }

    @Override
    public TagDtoResponse patch(Long id, JsonPatch patch) {
        return tagService.patch(id, patch);
    }
}
