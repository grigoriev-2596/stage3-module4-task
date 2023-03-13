package com.mjc.school.controller.implementation;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.controller.TagController;
import com.mjc.school.service.TagService;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import com.mjc.school.service.query.TagSearchCriteriaParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/tags")
public class TagControllerImpl implements TagController {

    private final TagService tagService;

    @Autowired
    public TagControllerImpl(TagService tagService) {
        this.tagService = tagService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @Override
    public List<TagDtoResponse> getTags(
            @PageableDefault(size = 30)
            @SortDefault(sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable,

            @RequestParam(required = false)
            String name,

            @RequestParam(required = false)
            Long newsId) {
        TagSearchCriteriaParams params = new TagSearchCriteriaParams(name, newsId);
        return tagService.getTags(pageable, params).getContent();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    @Override
    public List<TagDtoResponse> readAll() {
        return tagService.getAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id:\\d+}")
    @Override
    public TagDtoResponse readById(@PathVariable Long id) {
        return tagService.getById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Override
    public TagDtoResponse create(@Valid @RequestBody TagDtoRequest createRequest) {
        return tagService.create(createRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    @Override
    public TagDtoResponse update(@Valid @RequestBody TagDtoRequest updateRequest) {
        return tagService.update(updateRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id:\\d+}")
    @Override
    public boolean deleteById(@PathVariable Long id) {
        return tagService.deleteById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/{id:\\d+}", consumes = "application/json-patch+json")
    @Override
    public TagDtoResponse patch(@PathVariable Long id, @RequestBody JsonPatch patch) {
        return tagService.patch(id, patch);
    }
}
