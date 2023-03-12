package com.mjc.school.service.query;

import javax.validation.constraints.Size;
import java.util.List;

public record NewsSearchCriteriaParams(
        @Size(min = 5, max = 30, message = "News title must be between 5 and 30 characters long")
        String title,

        @Size(min = 5, max = 255, message = "News title must be between 5 and 30 characters long")
        String content,

        @Size(min = 3, max = 15, message = "Author name must be between 3 and 15 characters long")
        String authorName,

        List<String> tagNames,

        List<Long> tagIds) {
}
