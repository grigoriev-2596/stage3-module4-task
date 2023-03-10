package com.mjc.school.service.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public record NewsDtoRequest(
        @Min(1)
        Long id,

        @NotNull
        @Size(min = 5, max = 15, message = "News title must be between 5 and 15 characters long")
        String title,

        @NotNull
        @Size(min = 5, max = 255, message = "News content must be between 5 and 255 characters long")
        String content,

        @NotNull
        @Min(1)
        Long authorId,

        List<Long> tagIds) {
}
