package com.mjc.school.controller.implementation;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.controller.CommentController;
import com.mjc.school.service.CommentService;
import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.CommentDtoResponse;
import com.mjc.school.service.query.CommentSearchCriteriaParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/comments")
public class CommentControllerImpl implements CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentControllerImpl(CommentService commentService) {
        this.commentService = commentService;
    }


    @Override
    public List<CommentDtoResponse> getComments(Pageable pageable, String content, Long newsId) {
        CommentSearchCriteriaParams params = new CommentSearchCriteriaParams(content, newsId);
        return commentService.getComments(pageable, params).getContent();
    }

    @Override
    public CommentDtoResponse getById(Long id) {
        return commentService.getById(id);
    }

    @Override
    public CommentDtoResponse create(CommentDtoRequest createRequest) {
        return commentService.create(createRequest);
    }

    @Override
    public CommentDtoResponse update(Long id, CommentDtoRequest updateRequest) {
        return commentService.update(updateRequest);
    }

    @Override
    public Boolean deleteById(Long id) {
        return commentService.deleteById(id);
    }

    @Override
    public CommentDtoResponse patch(Long id, JsonPatch patch) {
        return commentService.patch(id, patch);
    }


}
