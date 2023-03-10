package com.mjc.school.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Validated
public interface TagController {

    @GetMapping
    List<TagDtoResponse> getTags(
            @PageableDefault(size = 30, page = 0)
            @SortDefault(sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable,

            @RequestParam(required = false)
            @Size(min = 3, max = 15, message = "Tag name must be between 3 and 15 characters long")
            String name,

            @RequestParam(required = false)
            @Min(1) Long newsId);

    @GetMapping(value = "/{id:\\d+}")
    TagDtoResponse getById(@Min(1) @PathVariable Long id);

    @PostMapping
    TagDtoResponse create(@Valid @RequestBody TagDtoRequest createRequest);

    @PutMapping(value = "/{id:\\d+}")
    TagDtoResponse update(@Min(1) @PathVariable Long id, @Valid @RequestBody TagDtoRequest updateRequest);

    @DeleteMapping(value = "/{id:\\d+}")
    Boolean deleteById(@Min(1) @PathVariable Long id);

    @PatchMapping(value = "/{id:\\d+}", consumes = "application/json-patch+json")
    TagDtoResponse patch(@Min(1) @PathVariable Long id, @RequestBody JsonPatch patch);

}
