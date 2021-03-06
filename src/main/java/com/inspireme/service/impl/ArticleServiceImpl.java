package com.inspireme.service.impl;

import com.inspireme.exception.NotFoundException;
import com.inspireme.model.Article;
import com.inspireme.model.Category;
import com.inspireme.model.Tag;
import com.inspireme.repository.ArticleRepository;
import com.inspireme.repository.CategoryRepository;
import com.inspireme.repository.TagRepository;
import com.inspireme.service.ArticleService;
import com.inspireme.service.TagService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    private final TagService tagService;

    private final static int MAX_RELATED_ARTICLES = 4;

    private final static long CATEGORY_WEIGHT = 2;
    private final static long TAG_WEIGHT = 1;

    public ArticleServiceImpl(ArticleRepository articleRepository, CategoryRepository categoryRepository, TagRepository tagRepository, TagService tagService) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.tagService = tagService;
    }

    @Override
    public Article retrieveArticle(Long articleId) {

        return articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundException(articleId, Article.class));
    }

    @Override
    public List<Article> retrieveAllArticles() {
        return articleRepository.findAll();
    }

    @Override
    public List<Article> retrieveAllArticlesPerCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(categoryId, Category.class));

        return articleRepository.findByCategory(category);
    }

    @Override
    public List<Article> retrieveAllArticlesPerTag(Long tagId) {
        return articleRepository.findByTag(tagService.retrieveTag(tagId));
    }

    @Override
    public Article saveArticle(Article article) {

        return articleRepository.save(article);
    }

    /**
     * After the user authentication is finalised, on the front end the article will be edited only by the logged-in
     * Admin user, hence updating the article publisher is not available through Postman
     */
    @Override
    public Article updateArticle(Article newArticle, Long articleId) {

        Article articleToUpdate = retrieveArticle(articleId);

        articleToUpdate.setArticleTitle(newArticle.getArticleTitle());
        articleToUpdate.setArticleText(newArticle.getArticleText());
        articleToUpdate.setImageUrl(newArticle.getImageUrl());
        articleToUpdate.setCategory(newArticle.getCategory());
        articleToUpdate.setTags(newArticle.getTags());

        return saveArticle(articleToUpdate);
    }

    @Override
    public void deleteArticle(Long articleId) {
        articleRepository.delete(retrieveArticle(articleId));
    }

    @Override
    public List<Article> retrieveRelatedArticles(Long targetArticleId) {

        Article targetArticle = retrieveArticle(targetArticleId);

        Comparator<Article> weightComparator = Comparator
                .comparing(article -> calculateRelevanceWeight(targetArticle, article),  Comparator.reverseOrder());
        Comparator<Article> datePublishedComparator = Comparator
                .comparing(Article::getDateArticlePublished, Comparator.reverseOrder());

       return retrieveAllArticles().stream()
                .filter(article -> !article.equals(targetArticle))
                .sorted(weightComparator.thenComparing(datePublishedComparator))
                .limit(MAX_RELATED_ARTICLES)
                .collect(Collectors.toList());
    }

    @Override
    public List<Tag> retrieveAllTagsPerArticle(Long articleId) {
        return tagRepository.findByArticle(retrieveArticle(articleId));
    }

    private long calculateRelevanceWeight(Article targetArticle, Article relatedArticle){
        long relevanceWeight = 0;

        if (targetArticle.getCategory().equals(relatedArticle.getCategory())){
            relevanceWeight += CATEGORY_WEIGHT;
        }

        long numberOfCommonTags = targetArticle.getTags().stream()
                .filter(tag -> relatedArticle.getTags().contains(tag))
                .count();

        relevanceWeight += (numberOfCommonTags * TAG_WEIGHT);

        return relevanceWeight;
    }
}


