package com.mjc.school.repository.impl;

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
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AuthorEntity> query = builder.createQuery(AuthorEntity.class);
        Root<AuthorEntity> root = query.from(AuthorEntity.class);

        if (params.name() != null) {
            query.where(builder.like(builder.lower(root.get("name")), "%" + params.name() + "%"));
        }
        if (params.newsId() != null) {
            Join<AuthorEntity, NewsEntity> newsJoin = root.join("news");
            query.where(builder.equal(newsJoin.get("id"), params.newsId()));
        }

        return getFilteredEntity(builder, query, root, pageable);
    }
}
