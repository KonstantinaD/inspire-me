package com.inspireme.service;

import com.inspireme.model.Article;
import com.inspireme.model.Category;
import com.inspireme.model.Tag;
import com.inspireme.repository.ArticleRepository;
import com.inspireme.service.impl.ArticleServiceImpl;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArticleServiceImplTestOne {

    @Mock
    private ArticleRepository mockArticleRepository;

    @InjectMocks
    private ArticleServiceImpl articleService;

    private final Category CATEGORY_1 = ArticleServiceImplTestHelper.buildCategory(1L);
    private final Category CATEGORY_2 = ArticleServiceImplTestHelper.buildCategory(2L);
    private final Category CATEGORY_3 = ArticleServiceImplTestHelper.buildCategory(3L);

    private final Tag TAG_1 = ArticleServiceImplTestHelper.buildTag(1L);
    private final Tag TAG_2 = ArticleServiceImplTestHelper.buildTag(2L);
    private final Tag TAG_3 = ArticleServiceImplTestHelper.buildTag(3L);
    private final Tag TAG_4 = ArticleServiceImplTestHelper.buildTag(4L);
    private final Tag TAG_5 = ArticleServiceImplTestHelper.buildTag(5L);

    private final Article ARTICLE_1 = ArticleServiceImplTestHelper.buildArticle(1L, CATEGORY_2,
            new HashSet<Tag>(){{add(TAG_1); add(TAG_2); add(TAG_3); add(TAG_4);}},
            LocalDateTime.of(2019, Month.JULY, 1, 19, 30, 40));

    private final Article ARTICLE_2 = ArticleServiceImplTestHelper.buildArticle(2L, CATEGORY_1,
            new HashSet<Tag>(){{add(TAG_2); add(TAG_4); add(TAG_5);}}, LocalDateTime.of(2019, Month.JULY,
                    1, 21, 0, 30));

    private final Article ARTICLE_3 = ArticleServiceImplTestHelper.buildArticle(3L, CATEGORY_3,
            new HashSet<Tag>(){{add(TAG_1); add(TAG_2); add(TAG_3);}}, LocalDateTime.of(2019, Month.JULY,
                    2, 1, 0, 0));

    private final Article ARTICLE_4 = ArticleServiceImplTestHelper.buildArticle(4L, CATEGORY_1,
            new HashSet<Tag>(){{add(TAG_3); add(TAG_5);}}, LocalDateTime.of(2019, Month.JULY,
                    2, 1, 0, 50));

    private final Article ARTICLE_5 = ArticleServiceImplTestHelper.buildArticle(5L, CATEGORY_1,
            new HashSet<Tag>(){{add(TAG_1);}}, LocalDateTime.of(2019, Month.JULY, 5, 10,
                    0, 0));

    private final Article ARTICLE_6 = ArticleServiceImplTestHelper.buildArticle(6L, CATEGORY_1,
            new HashSet<Tag>(){{add(TAG_1); add(TAG_2); add(TAG_3);}}, LocalDateTime.of(2019, Month.JULY,
                    5, 10, 20, 0));

    private final Article ARTICLE_7 = ArticleServiceImplTestHelper.buildArticle(7L, CATEGORY_2,
            new HashSet<Tag>(){{add(TAG_5);}}, LocalDateTime.of(2019, Month.JULY, 5, 10,
                    20, 0));

    @Before
    public void setUp(){

        ArrayList<Article> allArticles = Lists.newArrayList(ARTICLE_1, ARTICLE_2, ARTICLE_3, ARTICLE_4, ARTICLE_5,
                ARTICLE_6, ARTICLE_7);

        when(mockArticleRepository.findById(6L)).thenReturn(java.util.Optional.ofNullable(ARTICLE_6));

        when(mockArticleRepository.findById(7L)).thenReturn(java.util.Optional.ofNullable(ARTICLE_7));

        when(mockArticleRepository.findAll()).thenReturn(allArticles);
    }

    @Test
    public void sameRelevanceWeightArticles_returnsArticlesByDatePublishedNewerToOlder() {

        List<Article> articleList = articleService.retrieveRelatedArticles(ARTICLE_6.getArticleId());

        assertThat(articleList).containsExactly(ARTICLE_5, ARTICLE_4, ARTICLE_3, ARTICLE_2);
    }

    @Test
    public void someSameSomeDifferentRelevanceWeightArticles_returnsArticlesByWeightDescThenByDatePublishedNewerToOlder() {

        List<Article> articleList = articleService.retrieveRelatedArticles(ARTICLE_7.getArticleId());

        assertThat(articleList).containsExactly(ARTICLE_1, ARTICLE_4, ARTICLE_2, ARTICLE_6);
    }
}
