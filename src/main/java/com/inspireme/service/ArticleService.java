package com.inspireme.service;

import com.inspireme.model.Article;
import com.inspireme.model.Category;

import java.util.List;
import java.util.Optional;

public interface ArticleService {

    Optional<Article> retrieveArticle(Long articleId);

    List<Article> retrieveAllArticles();

    List<Article> retrieveAllArticlesPerCategory(Category category);

    Article saveArticle(Article article);

    void deleteArticle(Article article);

    List<Article> retrieveRelatedArticles(Article targetArticle);
}
