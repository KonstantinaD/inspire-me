package com.inspireme.exception;

public class CategoryNotFoundException extends RuntimeException {
    private Long categoryId;


    public CategoryNotFoundException(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() {
        return categoryId;
    }
}
