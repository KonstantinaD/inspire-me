package com.inspireme.service;

import com.inspireme.model.Article;
import com.inspireme.model.Category;
import com.inspireme.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    private final int MAX_RELATED_ARTICLES = 4;

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Optional<Article> retrieveArticle(Long articleId) {
        return articleRepository.findById(articleId);
    }

    public List<Article> retrieveAllArticles() {
        return articleRepository.findAll();
    }

    public List<Article> retrieveAllArticlesPerCategory(Category category) {
        return articleRepository.findByCategory(category);
    }

    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    public void deleteArticle(Article article) {
        articleRepository.delete(article);
    }

    public List<Article> retrieveRelatedArticles(Article targetArticle) {

        Category targetCategory = targetArticle.getCategory();

        List<Article> articlesInSameCategory = retrieveAllArticlesPerCategory(targetCategory)
            .stream()
            .filter(article -> !article.equals(targetArticle))
            .limit(MAX_RELATED_ARTICLES)
            .collect(Collectors.toList());

        if(articlesInSameCategory.size() < MAX_RELATED_ARTICLES){

            return Stream.concat(
                    articlesInSameCategory.stream(),
                    retrieveAllArticles().stream()
                            .filter(article -> !article.getCategory().equals(targetCategory))
                            .limit(MAX_RELATED_ARTICLES - articlesInSameCategory.size())
            ).collect(Collectors.toList());
        }

        return articlesInSameCategory;
    }
}
