package com.mjc.school.service.query;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public record AuthorSearchCriteriaParams(
        @Size(min = 3, max = 15, message = "Author name must be between 3 and 15 characters long")
        String name,

        @Min(1)
        Long newsId) {
}
