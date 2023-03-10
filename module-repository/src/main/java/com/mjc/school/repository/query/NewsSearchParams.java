package com.mjc.school.repository.query;

import java.util.List;

public record NewsSearchParams(
        String title,
        String content,
        String authorName,
        List<String> tagNames,
        List<Long> tagIds) {
}
