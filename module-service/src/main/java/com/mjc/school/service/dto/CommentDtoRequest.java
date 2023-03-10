package com.mjc.school.service.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record CommentDtoRequest(
        @Min(1)
        Long id,

        @NotNull(message = "Comment content is required")
        @Size(min = 5, max = 255, message = "Comment content must be between 5 and 255 characters long")
        String content,

        @Min(1)
        Long newsId) {
}
