package com.inspireme.servicelayer.serviceimplementations;

import com.inspireme.domainlayer.Article;
import com.inspireme.infrastructurelayer.repositories.ArticleRepository;
import com.inspireme.servicelayer.services.ArticleService;
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

    public List<Article> retrieveAllArticlesPerCategory(Long categoryId) {
        return articleRepository.findByCategoryId(categoryId);
    }

    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    public void deleteArticle(Long articleId) {
        articleRepository.deleteById(articleId);
    }


    private void validateCategoryId(Long categoryId) {
    }

    public List<Article> retrieveRelatedArticles(Long categoryId) {

        validateCategoryId(categoryId);

        List<Article> articlesInSameCategory = retrieveAllArticlesPerCategory(categoryId).stream()
                .limit(MAX_RELATED_ARTICLES)
                .collect(Collectors.toList());

        if(articlesInSameCategory.size() < MAX_RELATED_ARTICLES){

            return Stream.concat(
                    articlesInSameCategory.stream(),
                    retrieveAllArticles().stream()
                            .filter(article -> article.getCategory().getCategoryId() != categoryId)
                            .limit(MAX_RELATED_ARTICLES - articlesInSameCategory.size())
            ).collect(Collectors.toList());
        }

        return articlesInSameCategory;
    }

}
