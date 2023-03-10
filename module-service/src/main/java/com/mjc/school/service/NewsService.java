package com.mjc.school.service;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;
import com.mjc.school.service.query.NewsSearchCriteriaParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NewsService extends BaseService<NewsDtoRequest, NewsDtoResponse, Long> {

    Page<NewsDtoResponse> getNews(Pageable pageable, NewsSearchCriteriaParams params);

    NewsDtoResponse patch(Long id, JsonPatch patch);
}
