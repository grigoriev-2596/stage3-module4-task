package com.mjc.school.repository;

import com.mjc.school.repository.model.AuthorEntity;
import com.mjc.school.repository.query.AuthorSearchParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorRepository extends BaseRepository<AuthorEntity, Long> {

    Page<AuthorEntity> getAuthors(AuthorSearchParams params, Pageable pageable);
}
