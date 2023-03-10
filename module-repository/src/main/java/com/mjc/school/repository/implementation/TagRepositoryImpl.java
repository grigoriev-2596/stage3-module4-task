package com.mjc.school.repository.implementation;

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
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TagEntity> criteriaQuery = criteriaBuilder.createQuery(TagEntity.class);
        Root<TagEntity> root = criteriaQuery.from(TagEntity.class);
        Join<TagEntity, NewsEntity> newsJoin = root.join("news");

        if (params.name() != null) {
            criteriaQuery.where(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + params.name() + "%"));
        }
        if (params.newsId() != null) {
            criteriaQuery.where(criteriaBuilder.equal(newsJoin.get("id"), params.newsId()));
        }
        criteriaQuery.distinct(true);

        return getFilteredEntity(criteriaBuilder, criteriaQuery, root, pageable);
    }
}
