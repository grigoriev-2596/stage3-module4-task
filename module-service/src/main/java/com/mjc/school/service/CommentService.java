package com.mjc.school.service;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.CommentDtoResponse;
import com.mjc.school.service.query.CommentSearchCriteriaParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService extends BaseService<CommentDtoRequest, CommentDtoResponse, Long> {

    Page<CommentDtoResponse> getComments(Pageable pageable, CommentSearchCriteriaParams params);

    CommentDtoResponse patch(Long id, JsonPatch patch);

}
