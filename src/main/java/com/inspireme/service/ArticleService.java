package com.inspireme.service;

import com.inspireme.model.Article;
import com.inspireme.model.Tag;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.security.Principal;
import java.util.List;

public interface ArticleService {

    Article retrieveArticle(Long articleId);

    List<Article> retrieveAllArticles();

    List<Article> retrieveAllArticlesPerCategory(Long categoryId);

    List<Article> retrieveAllArticlesPerTag(Long tagId);

    List<Article> retrieveAllArticlesPerPublisher(Principal principal);

    Article saveArticle(Article article, OAuth2User principal);

    Article updateArticle(Article newArticle, Long articleId, OAuth2User principal);

    void deleteArticle(Long articleId);

    List<Article> retrieveRelatedArticles(Long targetArticleId);

    List<Tag> retrieveAllTagsPerArticle(Long articleId);
}
