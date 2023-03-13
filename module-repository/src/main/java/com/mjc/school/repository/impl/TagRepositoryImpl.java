package com.mjc.school.repository.impl;

import com.mjc.school.repository.TagRepository;
import com.mjc.school.repository.model.NewsEntity;
import com.mjc.school.repository.model.TagEntity;
import com.mjc.school.repository.query.TagSearchParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

@Repository
public class TagRepositoryImpl extends AbstractRepository<TagEntity, Long> implements TagRepository {

    @Override
    protected void setFields(TagEntity toUpdate, TagEntity updateBy) {
        toUpdate.setName(updateBy.getName());
    }

    @Override
    public Page<TagEntity> getTags(TagSearchParams params, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TagEntity> query = builder.createQuery(TagEntity.class);
        Root<TagEntity> root = query.from(TagEntity.class);

        if (params.name() != null) {
            query.where(builder.like(builder.lower(root.get("name")), "%" + params.name() + "%"));
        }
        if (params.newsId() != null) {
            Join<TagEntity, NewsEntity> newsJoin = root.join("news");
            query.where(builder.equal(newsJoin.get("id"), params.newsId()));
        }
        query.distinct(true);

        return getFilteredEntity(builder, query, root, pageable);
    }
}
