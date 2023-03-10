package com.mjc.school.service.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record TagDtoRequest(
        @Min(1)
        Long id,

        @NotNull
        @Size(min = 5, max = 15, message = "Tag name must be between 5 and 15 characters long")
        String name) {
}
