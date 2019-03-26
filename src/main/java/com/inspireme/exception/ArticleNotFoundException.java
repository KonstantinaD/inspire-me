package com.inspireme.exception;

public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException(Long articleId) {
        super("Could not find article " + articleId);
    }
}
