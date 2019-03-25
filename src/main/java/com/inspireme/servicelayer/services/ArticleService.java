package com.inspireme.servicelayer.services;

import com.inspireme.domainlayer.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleService {

    Optional<Article> retrieveArticle(Long articleId);

    List<Article> retrieveAllArticles();

    List<Article> retrieveAllArticlesPerCategory(Long categoryId);

    Article saveArticle(Article article);

    void deleteArticle(Long articleId);

    List<Article> retrieveRelatedArticles(Long categoryId);
}
