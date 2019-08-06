package com.inspireme.service;

import com.inspireme.model.Article;
import com.inspireme.model.Category;
import com.inspireme.model.Tag;
import com.inspireme.repository.ArticleRepository;
import com.inspireme.service.impl.ArticleServiceImpl;
import org.assertj.core.util.Lists;
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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)

public class ArticleServiceImplTestTwo {

    @Mock
    private ArticleRepository mockArticleRepository;

    @InjectMocks
    private ArticleServiceImpl articleService;

    private final Category CATEGORY_1 = ArticleServiceImplTestHelper.buildCategory(1L);
    private final Category CATEGORY_2 = ArticleServiceImplTestHelper.buildCategory(2L);

    private final Tag TAG_1 = ArticleServiceImplTestHelper.buildTag(1L);
    private final Tag TAG_2 = ArticleServiceImplTestHelper.buildTag(2L);
    private final Tag TAG_3 = ArticleServiceImplTestHelper.buildTag(3L);

    @Test
    public void totalNumberOfArticlesEqualsMaxNumberRelatedArticles_returnsAllArticlesExceptForTargetArticle() {

        final Article ARTICLE_1 = ArticleServiceImplTestHelper.buildArticle(1L, CATEGORY_2,
                new HashSet<Tag>(){{add(TAG_1); add(TAG_2); add(TAG_3);}},
                LocalDateTime.of(2019, Month.JULY, 1, 19, 30, 40));

        final Article ARTICLE_2 = ArticleServiceImplTestHelper.buildArticle(2L, CATEGORY_1,
                new HashSet<Tag>(){{add(TAG_2);}}, LocalDateTime.of(2019, Month.JULY,
                        1, 21, 0, 30));

        final Article ARTICLE_3 = ArticleServiceImplTestHelper.buildArticle(3L, CATEGORY_2,
                new HashSet<Tag>(){{add(TAG_1); add(TAG_2);}}, LocalDateTime.of(2019, Month.JULY,
                        2, 1, 0, 0));

        final Article ARTICLE_4 = ArticleServiceImplTestHelper.buildArticle(4L, CATEGORY_1,
                new HashSet<Tag>(){{add(TAG_2); add(TAG_3);}}, LocalDateTime.of(2019, Month.JULY,
                        2, 1, 0, 50));

        ArrayList<Article> allArticles = Lists.newArrayList(ARTICLE_1, ARTICLE_2, ARTICLE_3, ARTICLE_4);

        when(mockArticleRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(ARTICLE_1));

        when(mockArticleRepository.findAll()).thenReturn(allArticles);

        List<Article> articleList = articleService.retrieveRelatedArticles(ARTICLE_1.getArticleId());

        assertThat(articleList).containsExactly(ARTICLE_3, ARTICLE_4, ARTICLE_2);
    }

    @Test
    public void oneArticleInSystem_returnsNoRelatedArticles() {

        final Article ARTICLE_1 = ArticleServiceImplTestHelper.buildArticle(1L, CATEGORY_1,
                new HashSet<Tag>(){{add(TAG_1);}},
                LocalDateTime.of(2019, Month.JULY, 1, 19, 30, 40));

        ArrayList<Article> allArticles = Lists.newArrayList(ARTICLE_1);

        when(mockArticleRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(ARTICLE_1));

        when(mockArticleRepository.findAll()).thenReturn(allArticles);

        List<Article> articleList = articleService.retrieveRelatedArticles(ARTICLE_1.getArticleId());

        assertTrue(articleList.size() == 0);
    }
}
