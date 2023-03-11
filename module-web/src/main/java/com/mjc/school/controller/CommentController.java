package com.mjc.school.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.CommentDtoResponse;
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
public interface CommentController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    List<CommentDtoResponse> getComments(
            @PageableDefault(size = 20)
            @SortDefault(sort = "content", direction = Sort.Direction.ASC)
            Pageable pageable,

            @RequestParam(required = false)
            @Size(min = 5, max = 255, message = "Comment content must be between 5 and 255 characters long")
            String content,

            @RequestParam(required = false)
            @Min(1) Long newsId
    );

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id:\\d+}")
    CommentDtoResponse getById(@Min(1) @PathVariable Long id);

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    CommentDtoResponse create(@Valid @RequestBody CommentDtoRequest createRequest);

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id:\\d+}")
    CommentDtoResponse update(@Min(1) @PathVariable Long id, @Valid @RequestBody CommentDtoRequest updateRequest);

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id:\\d+}")
    Boolean deleteById(@Min(1) @PathVariable Long id);

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/{id:\\d+}", consumes = "application/json-patch+json")
    CommentDtoResponse patch(@Min(1) @PathVariable Long id, @RequestBody JsonPatch patch);

}
