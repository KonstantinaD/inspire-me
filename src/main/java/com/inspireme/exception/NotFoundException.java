package com.inspireme.exception;

public class NotFoundException extends RuntimeException {

    private Long id;
    private String entity;

    public NotFoundException(Long id, Class<?> klass) {
        this.id = id;
        this.entity = klass.getSimpleName();
    }

    public Long getId() {
        return id;
    }

    public String getEntity() {
        return entity;
    }
}
