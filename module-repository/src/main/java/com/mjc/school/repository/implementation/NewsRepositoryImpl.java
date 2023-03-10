package com.mjc.school.repository.implementation;

import com.mjc.school.repository.NewsRepository;
import com.mjc.school.repository.model.AuthorEntity;
import com.mjc.school.repository.model.NewsEntity;
import com.mjc.school.repository.model.TagEntity;
import com.mjc.school.repository.query.NewsSearchParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

@Repository
public class NewsRepositoryImpl extends AbstractRepository<NewsEntity, Long> implements NewsRepository {

    @Override
    protected void setFields(NewsEntity toUpdate, NewsEntity updateBy) {
        toUpdate.setTitle(updateBy.getTitle());
        toUpdate.setContent(updateBy.getContent());
        toUpdate.setAuthor(updateBy.getAuthor());
        toUpdate.setTags(updateBy.getTags());
    }

    @Override
    public Page<NewsEntity> getNews(NewsSearchParams params, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<NewsEntity> criteriaQuery = criteriaBuilder.createQuery(NewsEntity.class);
        Root<NewsEntity> root = criteriaQuery.from(NewsEntity.class);

        Join<NewsEntity, TagEntity> tagJoin = root.join("tags");
        Join<NewsEntity, AuthorEntity> authorJoin = root.join("author");

        if (params.tagIds() != null) {
            criteriaQuery.where(tagJoin.get("id").in(params.tagIds()));
        }

        if (params.tagNames() != null) {
            criteriaQuery.where(tagJoin.get("name").in(params.tagNames()));
        }
        if (params.authorName() != null) {
            criteriaQuery.where(
                    criteriaBuilder
                            .like(criteriaBuilder.lower(authorJoin.get("name")), "%" + params.authorName() + "%"));
        }
        if (params.title() != null) {
            criteriaQuery.where(
                    criteriaBuilder
                            .like(criteriaBuilder.lower(root.get("title")), "%" + params.title() + "%"));
        }
        if (params.content() != null) {
            criteriaQuery.where(
                    criteriaBuilder
                            .like(criteriaBuilder.lower(root.get("content")), "%" + params.content() + "%"));
        }
        criteriaQuery.distinct(true);

        return getFilteredEntity(criteriaBuilder, criteriaQuery, root, pageable);
    }

}
