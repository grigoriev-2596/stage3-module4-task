package com.mjc.school.service;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import com.mjc.school.service.query.TagSearchCriteriaParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TagService extends BaseService<TagDtoRequest, TagDtoResponse, Long> {

    Page<TagDtoResponse> getTags(Pageable pageable, TagSearchCriteriaParams params);

    TagDtoResponse patch(Long id, JsonPatch patch);
}
