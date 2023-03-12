package com.mjc.school.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.CommentDtoResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentController extends BaseController<CommentDtoRequest, CommentDtoResponse, Long> {

    List<CommentDtoResponse> getComments(Pageable pageable, String content, Long newsId);

    CommentDtoResponse patch(Long id, JsonPatch patch);
}
