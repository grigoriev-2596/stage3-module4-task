package com.mjc.school.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagController extends BaseController<TagDtoRequest, TagDtoResponse, Long> {

    List<TagDtoResponse> getTags(Pageable pageable, String name, Long newsId);

    TagDtoResponse patch(Long id, JsonPatch patch);

}
