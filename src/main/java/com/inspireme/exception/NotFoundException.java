package com.inspireme.exception;

public class NotFoundException extends RuntimeException {

    private Long id;
    private String entity;

    public NotFoundException(Long id, Class<?> clazz) {
        this.id = id;
        this.entity = clazz.getSimpleName();
    }

    public Long getId() {
        return id;
    }

    public String getEntity() {
        return entity;
    }
}
