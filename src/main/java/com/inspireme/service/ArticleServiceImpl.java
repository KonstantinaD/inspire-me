package com.inspireme.service;

import com.inspireme.exception.ArticleNotFoundException;
import com.inspireme.exception.CategoryNotFoundException;
import com.inspireme.exception.TagNotFoundException;
import com.inspireme.model.Article;
import com.inspireme.model.Category;
import com.inspireme.model.Tag;
import com.inspireme.repository.ArticleRepository;
import com.inspireme.repository.CategoryRepository;
import com.inspireme.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    private final int MAX_RELATED_ARTICLES = 4;

    private final long CATEGORY_WEIGHT = 2;
    private final long TAG_WEIGHT = 1;

    public ArticleServiceImpl(ArticleRepository articleRepository, CategoryRepository categoryRepository, TagRepository tagRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public Article retrieveArticle(Long articleId) {

        return articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException(articleId));
    }

    @Override
    public Article replaceArticle(Long articleId, Article newArticle) {

        Article updatedArticle = articleRepository.findById(articleId)
                .orElseGet(Article::new);

        updatedArticle.setArticleTitle(newArticle.getArticleTitle());
        updatedArticle.setArticleText(newArticle.getArticleText());
        updatedArticle.setImageUrl(newArticle.getImageUrl());
        updatedArticle.setCategory(newArticle.getCategory());
        updatedArticle.setArticlePublishedBy(newArticle.getArticlePublishedBy());

        return saveArticle(updatedArticle);
    }

    @Override
    public List<Article> retrieveAllArticles() {
        return articleRepository.findAll();
    }

    @Override
    public List<Article> retrieveAllArticlesPerCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        return articleRepository.findByCategory(category);
    }

    @Override
    public List<Article> retrieveAllArticlesPerTag(Long tagId) {
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new TagNotFoundException(tagId));

        return articleRepository.findByTags(tag);
    }

    @Override
    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    @Override
    public void deleteArticle(Article article) {
        articleRepository.delete(article);
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

    private long calculateRelevanceWeight(Article currentArticle, Article relatedArticle){
        long relevanceWeight = 0;

        if (currentArticle.getCategory().equals(relatedArticle.getCategory())){
            relevanceWeight += CATEGORY_WEIGHT;
        }

        long commonTags = currentArticle.getTags().stream()
                .filter(tag -> relatedArticle.getTags().contains(tag))
                .count();

        relevanceWeight += (commonTags * TAG_WEIGHT);

        return relevanceWeight;
    }
}


