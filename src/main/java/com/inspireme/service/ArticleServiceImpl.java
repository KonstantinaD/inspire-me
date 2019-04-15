package com.inspireme.service;

import com.inspireme.model.Article;
import com.inspireme.model.Category;
import com.inspireme.model.Tag;
import com.inspireme.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static jdk.nashorn.internal.objects.NativeMath.max;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    private final int MAX_RELATED_ARTICLES = 4;

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Optional<Article> retrieveArticle(Long articleId) {
        return articleRepository.findById(articleId);
    }

    @Override
    public List<Article> retrieveAllArticles() {
        return articleRepository.findAll();
    }

    @Override
    public List<Article> retrieveAllArticlesPerCategory(Category category) {
        return articleRepository.findByCategory(category);
    }

    @Override
    public List<Article> retrieveAllArticlesPerTag(Tag tag) {
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

        Optional<Article> targetArticle = retrieveArticle(targetArticleId);

        Category targetCategory = targetArticle.get().getCategory();

        List<Article> articlesInSameCategory = retrieveAllArticlesPerCategory(targetCategory)
            .stream()
            .filter(article -> !article.equals(targetArticle.get()))
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

//    @Override
//    public List<Article> retrieveRelatedArticlesByTags(Long targetArticleId) {
//
//        Optional<Article> targetArticle = retrieveArticle(targetArticleId);
//
//        Set<Tag> targetTags = targetArticle.get().getTags();
//
//        List<Article> articlesWithCommonTags1 = new ArrayList<Article>();
//
//        int numberOfCommonTags = 0;
//
//        for (Tag tag : targetTags) {
//
//            List<Article> articlesWithCommonTags = retrieveAllArticlesPerTag(tag)  //all articles which have this tag (excluding the current article)
//                    .stream()
//                    .filter(article -> !article.equals(targetArticle.get()))
//                    .collect(Collectors.toList());
//
//            for (Article article : articlesWithCommonTags) {
//                numberOfCommonTags = 1;
//                articlesWithCommonTags1.add(article);
//                Set<Tag> otherTagsOfArticleWithCommonTags = article.getTags()
//                        .stream()
//                        .filter(otherTag -> !otherTag.equals(tag))
//                        .collect(Collectors.toSet());
//
//                    for (Tag tag1 : otherTagsOfArticleWithCommonTags) {
//                        if (targetTags.contains(tag1)) {
//                        numberOfCommonTags =  numberOfCommonTags + 1;
//                        }
//                    }
//                }
//            }
//        return articlesWithCommonTags1
//                .stream()
//                .sorted(Comparator.comparingInt(max(numberOfCommonTags));
//
//
//        }
}
