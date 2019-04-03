package com.inspireme.exception;

public class ArticleNotFoundException extends RuntimeException {

    private Long articleId;

    public ArticleNotFoundException(Long articleId) {
        super("Could not find article " + articleId);
        this.articleId = articleId;
    }

    public Long getArticleId() {
        return articleId;
    }
}
