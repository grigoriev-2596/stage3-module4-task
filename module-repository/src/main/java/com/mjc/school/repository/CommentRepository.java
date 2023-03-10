package com.mjc.school.repository;

import com.mjc.school.repository.model.CommentEntity;
import com.mjc.school.repository.query.CommentSearchParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepository extends BaseRepository<CommentEntity, Long> {

    Page<CommentEntity> getComments(CommentSearchParams params, Pageable pageable);
}
