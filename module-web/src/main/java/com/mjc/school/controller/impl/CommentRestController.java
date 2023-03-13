package com.mjc.school.controller.impl;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.controller.CommentController;
import com.mjc.school.service.CommentService;
import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.CommentDtoResponse;
import com.mjc.school.service.query.CommentSearchCriteriaParams;
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
@RequestMapping(value = "/api/v1/comments")
public class CommentRestController implements CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @Override
    public List<CommentDtoResponse> getComments(
            @PageableDefault(size = 20)
            @SortDefault(sort = "content", direction = Sort.Direction.ASC)
            Pageable pageable,

            @RequestParam(required = false)
            String content,

            @RequestParam(required = false)
            Long newsId) {
        CommentSearchCriteriaParams params = new CommentSearchCriteriaParams(content, newsId);
        return commentService.getComments(pageable, params).getContent();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id:\\d+}")
    @Override
    public CommentDtoResponse readById(@PathVariable Long id) {
        return commentService.getById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Override
    public CommentDtoResponse create(@Valid @RequestBody CommentDtoRequest createRequest) {
        return commentService.create(createRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id:\\d+}")
    @Override
    public CommentDtoResponse update(@PathVariable Long id, @Valid @RequestBody CommentDtoRequest updateRequest) {
        return commentService.update(updateRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id:\\d+}")
    @Override
    public void deleteById(@PathVariable Long id) {
        commentService.deleteById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/{id:\\d+}", consumes = "application/json-patch+json")
    @Override
    public CommentDtoResponse patch(@PathVariable Long id, @RequestBody JsonPatch patch) {
        return commentService.patch(id, patch);
    }


}
