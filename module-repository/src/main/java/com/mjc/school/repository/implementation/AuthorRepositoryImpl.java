package com.mjc.school.repository.implementation;

import com.mjc.school.repository.AuthorRepository;
import com.mjc.school.repository.model.AuthorEntity;
import com.mjc.school.repository.model.NewsEntity;
import com.mjc.school.repository.query.AuthorSearchParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

@Repository
public class AuthorRepositoryImpl extends AbstractRepository<AuthorEntity, Long> implements AuthorRepository {

    @Override
    protected void setFields(AuthorEntity toUpdate, AuthorEntity updateBy) {
        toUpdate.setName(updateBy.getName());
    }

    @Override
    public Page<AuthorEntity> getAuthors(AuthorSearchParams params, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AuthorEntity> criteriaQuery = criteriaBuilder.createQuery(AuthorEntity.class);
        Root<AuthorEntity> root = criteriaQuery.from(AuthorEntity.class);
        Join<AuthorEntity, NewsEntity> newsJoin = root.join("news");

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
