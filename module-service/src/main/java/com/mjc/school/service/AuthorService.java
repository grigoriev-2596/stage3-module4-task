package com.mjc.school.service;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.query.AuthorSearchCriteriaParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService extends BaseService<AuthorDtoRequest, AuthorDtoResponse, Long> {

    Page<AuthorDtoResponse> getAuthors(AuthorSearchCriteriaParams params, Pageable pageable);

    AuthorDtoResponse patch(Long id, JsonPatch patch);
}
