package com.mjc.school.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
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
public interface AuthorController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    List<AuthorDtoResponse> getAuthors(
            @PageableDefault(size = 5)
            @SortDefault(sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable,

            @RequestParam(required = false)
            @Size(min = 3, max = 15, message = "Author name must be between 3 and 15 characters long")
            String name,

            @RequestParam(required = false)
            @Min(1)
            Long newsId
    );

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id:\\d+}")
    AuthorDtoResponse getById(@Min(1) @PathVariable Long id);

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    AuthorDtoResponse create(@Valid @RequestBody AuthorDtoRequest createRequest);

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id:\\d+}")
    AuthorDtoResponse update(@Min(1) @PathVariable Long id, @Valid @RequestBody AuthorDtoRequest updateRequest);

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id:\\d+}")
    Boolean deleteById(@Min(1) @PathVariable Long id);

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/{id:\\d+}", consumes = "application/json-patch+json")
    AuthorDtoResponse patch(@Min(1) @PathVariable Long id, @RequestBody JsonPatch patch);
}
