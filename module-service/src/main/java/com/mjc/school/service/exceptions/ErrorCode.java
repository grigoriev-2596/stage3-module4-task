package com.mjc.school.service.exceptions;

public enum ErrorCode {

    NEWS_DOES_NOT_EXIST(1001, "news with id %s does not exist"),
    AUTHOR_DOES_NOT_EXIST(1002, "author with id %s does not exist"),
    COMMENT_DOES_NOT_EXIST(1003, "comment with id %s does not exist"),
    TAG_DOES_NOT_EXIST(1004, "tag with id %s does not exist");

    private final int id;
    private final String message;

    ErrorCode(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ERROR_CODE: " + id + ", ERROR_MESSAGE: " + message;
    }
}
