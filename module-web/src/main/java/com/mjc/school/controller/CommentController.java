package com.mjc.school.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.CommentDtoResponse;
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
public interface CommentController {
    @GetMapping
    List<CommentDtoResponse> getComments(
            @PageableDefault(size = 20, page = 0)
            @SortDefault(sort = "content", direction = Sort.Direction.ASC)
            Pageable pageable,

            @RequestParam(required = false)
            @Size(min = 5, max = 255, message = "Comment content must be between 5 and 255 characters long")
            String content,

            @RequestParam(required = false)
            @Min(1) Long newsId
    );

    @GetMapping(value = "/{id:\\d+}")
    CommentDtoResponse getById(@Min(1) @PathVariable Long id);

    @PostMapping
    CommentDtoResponse create(@Valid @RequestBody CommentDtoRequest createRequest);

    @PutMapping(value = "/{id:\\d+}")
    CommentDtoResponse update(@Min(1) @PathVariable Long id, @Valid @RequestBody CommentDtoRequest updateRequest);

    @DeleteMapping(value = "/{id:\\d+}")
    Boolean deleteById(@Min(1) @PathVariable Long id);

    @PatchMapping(value = "/{id:\\d+}", consumes = "application/json-patch+json")
    CommentDtoResponse patch(@Min(1) @PathVariable Long id, @RequestBody JsonPatch patch);

}
