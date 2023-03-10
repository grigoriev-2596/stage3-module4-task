package com.mjc.school.repository;

import com.mjc.school.repository.model.NewsEntity;
import com.mjc.school.repository.query.NewsSearchParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NewsRepository extends BaseRepository<NewsEntity, Long> {

    Page<NewsEntity> getNews(NewsSearchParams params, Pageable pageable);
}