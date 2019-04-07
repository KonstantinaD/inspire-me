package com.inspireme.exception;

public class TagNotFoundException extends RuntimeException {

    private Long tagId;

    public TagNotFoundException(Long tagId) {
        this.tagId = tagId;
    }

    public Long getTagId() {
        return tagId;
    }
}
