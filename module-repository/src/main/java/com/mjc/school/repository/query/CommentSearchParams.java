package com.mjc.school.repository.query;

public record CommentSearchParams(
        String content,
        Long newsId) {
}
