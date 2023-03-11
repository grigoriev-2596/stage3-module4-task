package com.mjc.school.controller.implementation;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.controller.AuthorController;
import com.mjc.school.service.AuthorService;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.query.AuthorSearchCriteriaParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/authors")
public class AuthorControllerImpl implements AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorControllerImpl(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Override
    public List<AuthorDtoResponse> getAuthors(Pageable pageable, String name, Long newsId) {
        AuthorSearchCriteriaParams params = new AuthorSearchCriteriaParams(name, newsId);
        return authorService.getAuthors(pageable, params).getContent();
    }

    @Override
    public AuthorDtoResponse getById(Long id) {
        return authorService.getById(id);
    }

    @Override
    public AuthorDtoResponse create(AuthorDtoRequest createRequest) {
        return authorService.create(createRequest);
    }

    @Override
    public AuthorDtoResponse update(Long id, AuthorDtoRequest updateRequest) {
        return authorService.update(updateRequest);
    }

    @Override
    public Boolean deleteById(Long id) {
        return authorService.deleteById(id);
    }

    @Override
    public AuthorDtoResponse patch(Long id, JsonPatch patch) {
        return authorService.patch(id, patch);
    }

}
