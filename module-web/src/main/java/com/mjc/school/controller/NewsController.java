package com.mjc.school.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Validated
public interface NewsController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    List<NewsDtoResponse> getNews(
            @PageableDefault(size = 2)
            @SortDefault(sort = "title", direction = Sort.Direction.ASC)
            Pageable pageable,

            @RequestParam(required = false)
            @Size(max = 30, message = "News title must be between 5 and 30 characters long")
            String title,
            @RequestParam(required = false)
            @Size(min = 5, max = 255, message = "News title must be between 5 and 30 characters long")
            String content,
            @RequestParam(required = false)
            @Size(min = 3, max = 15, message = "Author name must be between 3 and 15 characters long")
            String authorName,
            @RequestParam(required = false)
            List<String> tagNames,
            @RequestParam(required = false)
            List<Long> tagIds);

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id:\\d+}")
    NewsDtoResponse getById(@Min(1) @PathVariable Long id);

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    NewsDtoResponse create(@Valid @RequestBody NewsDtoRequest createRequest);

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id:\\d+}")
    NewsDtoResponse update(@Min(1) @PathVariable Long id, @Valid @RequestBody NewsDtoRequest updateRequest);

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id:\\d+}")
    Boolean deleteById(@Min(1) @PathVariable Long id);

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/{id:\\d+}", consumes = "application/json-patch+json")
    NewsDtoResponse patch(@Min(1) @PathVariable Long id, @RequestBody JsonPatch patch);
}
