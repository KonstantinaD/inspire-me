package com.inspireme.service;

import com.inspireme.model.Article;

import java.util.List;

public interface ArticleService {

    Article retrieveArticle(Long articleId);

    Article updateArticle(Article newArticle, Long articleId);

    List<Article> retrieveAllArticles();

    List<Article> retrieveAllArticlesPerCategory(Long categoryId);

    List<Article> retrieveAllArticlesPerTag(Long tagId);

    Article saveArticle(Article article);

    void deleteArticle(Long articleId);

    List<Article> retrieveRelatedArticles(Long targetArticleId);
}
