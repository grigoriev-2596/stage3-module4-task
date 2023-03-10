package com.mjc.school.service.query;

import java.util.List;

public record NewsSearchCriteriaParams(
        String title,
        String content,
        String authorName,
        List<String> tagNames,
        List<Long> tagIds) {
}
