package com.mjc.school.repository.impl;

import com.mjc.school.repository.CommentRepository;
import com.mjc.school.repository.model.CommentEntity;
import com.mjc.school.repository.query.CommentSearchParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class CommentRepositoryImpl extends AbstractRepository<CommentEntity, Long> implements CommentRepository {

    @Override
    protected void setFields(CommentEntity toUpdate, CommentEntity updateBy) {
        toUpdate.setContent(updateBy.getContent());
    }

    @Override
    public Page<CommentEntity> getComments(CommentSearchParams params, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CommentEntity> criteriaQuery = builder.createQuery(CommentEntity.class);
        Root<CommentEntity> root = criteriaQuery.from(CommentEntity.class);

        if (params.content() != null) {
            criteriaQuery.where(builder.like(builder.lower(root.get("content")), "%" + params.content() + "%"));
        }
        if (params.newsId() != null) {
            criteriaQuery.where(builder.equal(root.get("news").get("id"), params.newsId()));
        }
        criteriaQuery.distinct(true);

        return getFilteredEntity(builder, criteriaQuery, root, pageable);
    }
}
