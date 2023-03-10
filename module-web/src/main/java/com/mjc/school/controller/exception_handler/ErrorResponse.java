package com.mjc.school.controller.exception_handler;

import java.util.List;

public record ErrorResponse(
        String message,
        List<String> details
) {
}
