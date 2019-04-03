package com.inspireme.exception;

import com.inspireme.model.Article;

public class ArticleNotFoundException extends RuntimeException {

    public ArticleNotFoundException(Article article) {
        super("Could not find article " + article);
    }

    public ArticleNotFoundException() {
        super("Could not find article");
    }
}
