package com.mjc.school.service.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record AuthorDtoRequest(
        @Min(1)
        Long id,

        @NotNull(message = "Author name is required")
        @Size(min = 3, max = 15, message = "Author name must be between 3 and 15 characters long")
        String name) {
}
