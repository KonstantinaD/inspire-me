package com.inspireme.service;

import com.inspireme.model.Article;

import java.util.List;

public interface ArticleService {

    Article retrieveArticle(Long articleId);

    Article replaceArticle(Long articleId, Article newArticle);

    List<Article> retrieveAllArticles();

    List<Article> retrieveAllArticlesPerCategory(Long categoryId);

    List<Article> retrieveAllArticlesPerTag(Long tagId);

    Article saveArticle(Article article);

    void deleteArticle(Article article);

    List<Article> retrieveRelatedArticles(Long targetArticleId);
}
