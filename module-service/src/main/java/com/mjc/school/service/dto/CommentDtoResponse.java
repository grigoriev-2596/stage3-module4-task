package com.mjc.school.service.dto;

import java.time.LocalDateTime;

public record CommentDtoResponse(
        Long id,
        String content,
        Long newsId,
        LocalDateTime creationDate,
        LocalDateTime lastUpdateDate) {
}
