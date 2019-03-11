package com.inspireme.presentationlayer.notfoundexceptions;

public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException(Long articleId) {
        super("Could not find article " + articleId);
    }
}
