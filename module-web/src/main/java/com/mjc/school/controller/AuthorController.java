package com.mjc.school.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthorController extends BaseController<AuthorDtoRequest, AuthorDtoResponse, Long> {

    List<AuthorDtoResponse> getAuthors(Pageable pageable, String name, Long newsId);

    AuthorDtoResponse patch(Long id, JsonPatch patch);
}
