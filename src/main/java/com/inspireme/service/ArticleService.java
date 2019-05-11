package com.inspireme.service;

import com.inspireme.model.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleService {

    Optional<Article> retrieveArticle(Long articleId);

    List<Article> retrieveAllArticles();

    List<Article> retrieveAllArticlesPerCategory(Long categoryId);

    List<Article> retrieveAllArticlesPerTag(Long tagId);

    Article saveArticle(Article article);

    void deleteArticle(Article article);

    List<Article> retrieveRelatedArticles(Long targetArticleId);

//    List<Article> retrieveRelatedArticlesByTags(Long targetArticleId);
}
