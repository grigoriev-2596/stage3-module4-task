package com.mjc.school.repository;

import com.mjc.school.repository.model.TagEntity;
import com.mjc.school.repository.query.TagSearchParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TagRepository extends BaseRepository<TagEntity, Long> {

    Page<TagEntity> getTags(TagSearchParams params, Pageable pageable);
}
