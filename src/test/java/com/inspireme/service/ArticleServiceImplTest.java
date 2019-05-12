package com.inspireme.service;

import com.inspireme.model.Article;
import com.inspireme.model.Category;
import com.inspireme.repository.ArticleRepository;
import com.inspireme.service.impl.ArticleServiceImpl;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArticleServiceImplTest {

    @Mock
    private ArticleRepository mockArticleRepository;

    @InjectMocks
    private ArticleServiceImpl articleService;

    private final Category CATEGORY_1 = buildCategory(1L);
    private final Category CATEGORY_2 = buildCategory(2L);
    private final Category CATEGORY_3 = buildCategory(3L);

    private final Article ARTICLE_1 = buildArticle(1L, CATEGORY_1);
    private final Article ARTICLE_2 = buildArticle(2L, CATEGORY_1);
    private final Article ARTICLE_3 = buildArticle(3L, CATEGORY_2);
    private final Article ARTICLE_4 = buildArticle(4L, CATEGORY_2);
    private final Article ARTICLE_5 = buildArticle(5L, CATEGORY_2);
    private final Article ARTICLE_6 = buildArticle(6L, CATEGORY_1);
    private final Article ARTICLE_7 = buildArticle(7L, CATEGORY_3);
    private final Article ARTICLE_8 = buildArticle(8L, CATEGORY_2);
    private final Article ARTICLE_9 = buildArticle(9L, CATEGORY_2);

    @Before
    public void setUp(){

        when(mockArticleRepository.findAll()).thenReturn(
                Lists.newArrayList(ARTICLE_1, ARTICLE_2, ARTICLE_3, ARTICLE_4, ARTICLE_5, ARTICLE_6)
        );

        when(mockArticleRepository.findByCategory(CATEGORY_1)).thenReturn(
                Lists.newArrayList(ARTICLE_1, ARTICLE_2, ARTICLE_6)
        );

        when(mockArticleRepository.findByCategory(CATEGORY_2)).thenReturn(
                Lists.newArrayList(ARTICLE_3, ARTICLE_4, ARTICLE_5, ARTICLE_8, ARTICLE_9)
        );

        when(mockArticleRepository.findByCategory(CATEGORY_3)).thenReturn(
                Lists.newArrayList(ARTICLE_7)
        );
    }

    @Test
    public void sameCategoryArticlesLessThanMax_returnSameAndDifferentArticleCategories() {

        List<Article> articleList = articleService.retrieveRelatedArticles(ARTICLE_1.getArticleId());

        assertThat(articleList).containsExactly(ARTICLE_2, ARTICLE_6, ARTICLE_3, ARTICLE_4);
    }

    @Test
    public void sameCategoryArticlesEqualToMax_returnSameArticleCategories() {

        List<Article> articleList = articleService.retrieveRelatedArticles(ARTICLE_9.getArticleId());

        assertThat(articleList).containsExactly(ARTICLE_3, ARTICLE_4, ARTICLE_5, ARTICLE_8);
    }

    @Test
    public void noOtherSameCategoryArticles_returnDifferentArticleCategories() {

        List<Article> articleList = articleService.retrieveRelatedArticles(ARTICLE_7.getArticleId());

        assertThat(articleList).containsExactly(ARTICLE_1, ARTICLE_2, ARTICLE_3, ARTICLE_4);
    }

    private Category buildCategory(Long categoryId){
        return Category.builder().categoryId(categoryId).categoryName("Category " + categoryId).build();
    }

    private Article buildArticle(Long articleId, Category category){
        return Article.builder().articleId(articleId).category(category).build();
    }
}
