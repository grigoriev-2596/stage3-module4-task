package com.mjc.school.controller.impl;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.controller.AuthorController;
import com.mjc.school.service.AuthorService;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.query.AuthorSearchCriteriaParams;
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
@RequestMapping(value = "/api/v1/authors")
public class AuthorRestController implements AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorRestController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @Override
    public List<AuthorDtoResponse> getAuthors(
            @PageableDefault(size = 5)
            @SortDefault(sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable,

            @RequestParam(required = false)
            String name,

            @RequestParam(required = false)
            Long newsId) {
        AuthorSearchCriteriaParams params = new AuthorSearchCriteriaParams(name, newsId);
        return authorService.getAuthors(pageable, params).getContent();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id:\\d+}")
    @Override
    public AuthorDtoResponse readById(@PathVariable Long id) {
        return authorService.getById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Override
    public AuthorDtoResponse create(@Valid @RequestBody AuthorDtoRequest createRequest) {
        return authorService.create(createRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id:\\d+}")
    @Override
    public AuthorDtoResponse update(@PathVariable Long id, @Valid @RequestBody AuthorDtoRequest updateRequest) {
        return authorService.update(updateRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id:\\d+}")
    @Override
    public void deleteById(@PathVariable Long id) {
        authorService.deleteById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/{id:\\d+}", consumes = "application/json-patch+json")
    @Override
    public AuthorDtoResponse patch(@PathVariable Long id, @RequestBody JsonPatch patch) {
        return authorService.patch(id, patch);
    }

}
