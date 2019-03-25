package com.inspireme.servicelayer.serviceimplementations;

import com.inspireme.domainlayer.Article;
import com.inspireme.infrastructurelayer.repositories.ArticleRepository;
import com.inspireme.servicelayer.services.ArticleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

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

    public List<Article> retrieveRelatedArticles(Long categoryId) {

        if (!retrieveAllArticles().isEmpty() && (categoryId >= 1) && (categoryId <= 4)) {
            List<Article> relatedArticles = new ArrayList<Article>();

            List<Article> allArticles = retrieveAllArticles();

            int numberOfAllArticles = retrieveAllArticles().size();

            //if (!retrieveAllArticlesPerCategory(categoryId).isEmpty()) {
            List<Article> articlesInSameCategory = retrieveAllArticlesPerCategory(categoryId);
            int numberOfArticlesInSameCategory = retrieveAllArticlesPerCategory(categoryId).size();

            if (numberOfArticlesInSameCategory >= 4) {
                relatedArticles = retrieveAllArticlesPerCategory(categoryId).subList(0, 4);
                return relatedArticles;
            }


            /*if (numberOfArticlesInSameCategory >= 1 && numberOfArticlesInSameCategory < 4)*/
            else {

                for (Article article : articlesInSameCategory) {
                    relatedArticles.add(article);
                }

                int maxNumberOfArticlesFromOtherCategoriesToAdd = 4 - numberOfArticlesInSameCategory;
                //  if (numberOfArticlesFromOtherCategoriesToAdd >= 3) {
                    for (int num = 0; num < maxNumberOfArticlesFromOtherCategoriesToAdd; num++) {
                       //if (maxNumberOfArticlesFromOtherCategoriesToAdd >= 3) {
                        Article articleFromOtherCategoriesToAdd = articleRepository.findFromOtherCategoryIds(categoryId).get(num);
                        relatedArticles.add(articleFromOtherCategoriesToAdd);
                    }
//                    else {
//                    Article articleToAdd = retrieveAllArticles().get(maxNumberOfArticlesFromOtherCategoriesToAdd - num);
//                    relatedArticles.add(articleToAdd);
//                }

                }
                return relatedArticles;

//                    } else {
//                        return relatedArticles;// allArticles.subList(0, 4);
//                    }
//                }
            }
//            else {
//                if (retrieveAllArticles().size() < 4) {
//                    return retrieveAllArticles().subList(0, retrieveAllArticles().size());
//                } else {
//                    return retrieveAllArticles().subList(0, 4);
//                }
//            }
        //}
            return null;
    }
}
