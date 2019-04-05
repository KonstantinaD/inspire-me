package com.inspireme.exception;

public class ArticleNotFoundException extends RuntimeException {

    private Long articleId;

    public ArticleNotFoundException(Long articleId) {
        this.articleId = articleId;
    }

    public Long getArticleId() {
        return articleId;
    }
}
