package com.inspireme.service;

import com.inspireme.exception.CategoryNotFoundException;
import com.inspireme.exception.TagNotFoundException;
import com.inspireme.model.Article;
import com.inspireme.model.Category;
import com.inspireme.model.Tag;
import com.inspireme.repository.ArticleRepository;
import com.inspireme.repository.CategoryRepository;
import com.inspireme.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    private final int MAX_RELATED_ARTICLES = 4;

    public ArticleServiceImpl(ArticleRepository articleRepository, CategoryRepository categoryRepository, TagRepository tagRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
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

        Optional<Article> targetArticle = retrieveArticle(targetArticleId);

        Category targetCategory = targetArticle.get().getCategory();

        List<Article> articlesInSameCategory = retrieveAllArticlesPerCategory(targetCategory.getCategoryId())
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
