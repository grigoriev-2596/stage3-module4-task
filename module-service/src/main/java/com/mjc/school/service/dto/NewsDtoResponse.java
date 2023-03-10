package com.mjc.school.service.dto;

import java.time.LocalDateTime;
import java.util.List;

public record NewsDtoResponse(
        Long id,
        String title,
        String content,
        LocalDateTime creationDate,
        LocalDateTime lastUpdateDate,
        AuthorDtoResponse author,
        List<TagDtoResponse> tags) {
}
