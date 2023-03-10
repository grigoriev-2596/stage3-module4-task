package com.mjc.school.repository.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "news")
@EntityListeners(AuditingEntityListener.class)
public class NewsEntity implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "news_generator")
    @SequenceGenerator(name = "news_generator", sequenceName = "news_seq", allocationSize = 1)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column(name = "creation_date", updatable = false)
    @CreatedDate
    private LocalDateTime creationDate;

    @Column(name = "last_update_date")
    @LastModifiedDate
    private LocalDateTime lastUpdateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private AuthorEntity author;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "news_tags",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<TagEntity> tags = new ArrayList<>();

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "news", fetch = FetchType.LAZY)
    private List<CommentEntity> comments = new ArrayList<>();

    public static Builder newBuilder() {
        return new NewsEntity().new Builder();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public AuthorEntity getAuthor() {
        return author;
    }

    public void setAuthor(AuthorEntity author) {
        this.author = author;
    }

    public List<TagEntity> getTags() {
        return tags;
    }

    public void setTags(List<TagEntity> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsEntity newsModel = (NewsEntity) o;
        return Objects.equals(id, newsModel.id) && Objects.equals(title, newsModel.title) && Objects.equals(content, newsModel.content) && Objects.equals(creationDate, newsModel.creationDate) && Objects.equals(lastUpdateDate, newsModel.lastUpdateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, creationDate, lastUpdateDate);
    }

    @Override
    public String toString() {
        return "NewsModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", creationDate=" + creationDate +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }

    public class Builder {

        private Builder() {

        }

        public Builder setId(Long id) {
            NewsEntity.this.id = id;
            return this;
        }

        public Builder setComments(List<CommentEntity> comments) {
            NewsEntity.this.comments = comments;
            return this;
        }

        public Builder setTitle(String title) {
            NewsEntity.this.title = title;
            return this;
        }

        public Builder setContent(String content) {
            NewsEntity.this.content = content;
            return this;
        }

        public Builder setCreationDate(LocalDateTime creationDate) {
            NewsEntity.this.creationDate = creationDate;
            return this;
        }

        public Builder setLastUpdateDate(LocalDateTime lastUpdateDate) {
            NewsEntity.this.lastUpdateDate = lastUpdateDate;
            return this;
        }


        public Builder setAuthor(AuthorEntity author) {
            NewsEntity.this.author = author;
            return this;
        }

        public Builder setTags(List<TagEntity> tags) {
            NewsEntity.this.tags = tags;
            return this;
        }

        public NewsEntity Build() {
            return NewsEntity.this;
        }
    }
}
