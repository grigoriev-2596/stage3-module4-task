package com.mjc.school.repository.implementation;

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
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CommentEntity> criteriaQuery = criteriaBuilder.createQuery(CommentEntity.class);
        Root<CommentEntity> root = criteriaQuery.from(CommentEntity.class);

        if (params.content() != null) {
            criteriaQuery.where(criteriaBuilder.like(criteriaBuilder.lower(root.get("content")), "%" + params.content() + "%"));
        }
        if (params.newsId() != null) {
            criteriaQuery.where(criteriaBuilder.equal(root.get("news").get("id"), params.newsId()));
        }
        criteriaQuery.distinct(true);

        return getFilteredEntity(criteriaBuilder, criteriaQuery, root, pageable);
    }
}
