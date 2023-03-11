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
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<NewsEntity> query = builder.createQuery(NewsEntity.class);
        Root<NewsEntity> root = query.from(NewsEntity.class);

        if (params.tagIds() != null || params.tagNames() != null) {
            Join<NewsEntity, TagEntity> tagJoin = root.join("tags");
            if (params.tagIds() != null) {
                query.where(tagJoin.get("id").in(params.tagIds()));
            }
            if (params.tagNames() != null) {
                query.where(tagJoin.get("name").in(params.tagNames()));
            }
        }

        if (params.authorName() != null) {
            Join<NewsEntity, AuthorEntity> authorJoin = root.join("author");
            query.where(builder.like(builder.lower(authorJoin.get("name")), "%" + params.authorName() + "%"));
        }
        if (params.title() != null) {
            query.where(builder.like(builder.lower(root.get("title")), "%" + params.title() + "%"));
        }
        if (params.content() != null) {
            query.where(builder.like(builder.lower(root.get("content")), "%" + params.content() + "%"));
        }

        return getFilteredEntity(builder, query, root, pageable);
    }

}
