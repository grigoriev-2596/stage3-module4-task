package com.mjc.school.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NewsController extends BaseController<NewsDtoRequest, NewsDtoResponse, Long> {

    List<NewsDtoResponse> getNews(Pageable pageable, String title, String content, String authorName, List<String> tagNames, List<Long> tagIds);

    NewsDtoResponse patch(Long id, JsonPatch patch);
}
