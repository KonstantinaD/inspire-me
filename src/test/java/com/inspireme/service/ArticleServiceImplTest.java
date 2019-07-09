package com.inspireme.service;

import com.inspireme.model.Article;
import com.inspireme.model.Category;
import com.inspireme.model.Tag;
import com.inspireme.repository.ArticleRepository;
import com.inspireme.service.impl.ArticleServiceImpl;
import org.assertj.core.api.ListAssert;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
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

    private final Tag TAG_1 = buildTag(1L);
    private final Tag TAG_2 = buildTag(2L);
    private final Tag TAG_3 = buildTag(3L);
    private final Tag TAG_4 = buildTag(4L);
    private final Tag TAG_5 = buildTag(5L);

    private final Article ARTICLE_1 = buildArticle(1L, CATEGORY_1, new HashSet<Tag>(){{add(TAG_1); add(TAG_2); add(TAG_3); /*add(TAG_4);*/}}, LocalDateTime.of(2019, Month.JULY, 5, 10, 20, 0)); //target

    private final Article ARTICLE_2 = buildArticle(2L, CATEGORY_1, new HashSet<Tag>(){{add(TAG_1); /*add(TAG_2); add(TAG_5);*/}}, LocalDateTime.of(2019, Month.JULY, 5, 10, 0, 0)); //weight 3

    private final Article ARTICLE_3 = buildArticle(3L, CATEGORY_1, new HashSet<Tag>(){{add(TAG_3); add(TAG_5);}}, LocalDateTime.of(2019, Month.JULY, 2, 1, 0, 50)); //3

    private final Article ARTICLE_4 = buildArticle(4L, CATEGORY_3, new HashSet<Tag>(){{add(TAG_1); /*add(TAG_2);*/ add(TAG_3); add(TAG_4);}}, LocalDateTime.of(2019, Month.JULY, 2, 1, 0, 0));//3

    private final Article ARTICLE_5 = buildArticle(5L, CATEGORY_1, new HashSet<Tag>(){{add(TAG_2); add(TAG_4); add(TAG_5);}}, LocalDateTime.of(2019, Month.JULY, 1, 21, 0, 30)); //3

    private final Article ARTICLE_6 = buildArticle(6L, CATEGORY_2, new HashSet<Tag>(){{add(TAG_1); add(TAG_2); add(TAG_3); add(TAG_4);}}, LocalDateTime.of(2019, Month.JULY, 1, 19, 30, 40)); //3

    @Before
    public void setUp(){

        ArrayList<Article> allArticles = Lists.newArrayList(ARTICLE_1, ARTICLE_2, ARTICLE_3, ARTICLE_4, ARTICLE_5, ARTICLE_6);

        ArrayList<Article> articlesCategory1 = Lists.newArrayList(ARTICLE_1, ARTICLE_2, ARTICLE_3, ARTICLE_5);

        ArrayList<Article> articlesCategory2 = Lists.newArrayList(ARTICLE_6);

        ArrayList<Article> articlesCategory3 = Lists.newArrayList(ARTICLE_4);

        ArrayList<Article> articlesTag1 = Lists.newArrayList(ARTICLE_1, ARTICLE_2, ARTICLE_4, ARTICLE_6);

        ArrayList<Article> articlesTag2 = Lists.newArrayList(ARTICLE_1, ARTICLE_5, ARTICLE_6);

        ArrayList<Article> articlesTag3 = Lists.newArrayList(ARTICLE_1, ARTICLE_3, ARTICLE_4, ARTICLE_6);

        ArrayList<Article> articlesTag4 = Lists.newArrayList(ARTICLE_4, ARTICLE_5, ARTICLE_6);

        ArrayList<Article> articlesTag5 = Lists.newArrayList(ARTICLE_3, ARTICLE_5);

        when(mockArticleRepository.findAll()).thenReturn(allArticles);

        when(mockArticleRepository.findByCategory(CATEGORY_1)).thenReturn(articlesCategory1);

        when(mockArticleRepository.findByCategory(CATEGORY_2)).thenReturn(articlesCategory2);

        when(mockArticleRepository.findByCategory(CATEGORY_3)).thenReturn(articlesCategory3);

        when(mockArticleRepository.findByTag(TAG_1)).thenReturn(articlesTag1);

        when(mockArticleRepository.findByTag(TAG_2)).thenReturn(articlesTag2);

        when(mockArticleRepository.findByTag(TAG_3)).thenReturn(articlesTag3);

        when(mockArticleRepository.findByTag(TAG_4)).thenReturn(articlesTag4);

        when(mockArticleRepository.findByTag(TAG_5)).thenReturn(articlesTag5);

//        Comparator<ListAssert<Article>> datePublishedComparator = Comparator
//                .comparing(Article::getDateArticlePublished, Comparator.reverseOrder());
    }

    @Test
    public void sameRelevanceWeightArticles_returnArticlesByDatePublishedNewerToOlder() {
//        Comparator<ListAssert<Article>> datePublishedComparator = Comparator
//                .comparing(/*Article::getDateArticlePublished*/article -> article.getClass(), Comparator.reverseOrder());

        List<Article> articleList = articleService.retrieveRelatedArticles(ARTICLE_1.getArticleId());
        assertNotNull(ARTICLE_1.getArticleId());

        assertThat(articleList).containsExactly(ARTICLE_2, ARTICLE_3, ARTICLE_4, ARTICLE_5)/*.usingComparator(datePublishedComparator)*/;
    }

//    @Test
//    public void sameCategoryArticlesLessThanMax_returnSameAndDifferentArticleCategories() {
//
//        List<Article> articleList = articleService.retrieveRelatedArticles(ARTICLE_1.getArticleId());
//
//        assertThat(articleList).containsExactly(ARTICLE_2, ARTICLE_6, ARTICLE_3, ARTICLE_4);
//    }
//
//    @Test
//    public void sameCategoryArticlesEqualToMax_returnSameArticleCategories() {
//
//        List<Article> articleList = articleService.retrieveRelatedArticles(ARTICLE_9.getArticleId());
//
//        assertThat(articleList).containsExactly(ARTICLE_3, ARTICLE_4, ARTICLE_5, ARTICLE_8);
//    }
//
//    @Test
//    public void noOtherSameCategoryArticles_returnDifferentArticleCategories() {
//
//        List<Article> articleList = articleService.retrieveRelatedArticles(ARTICLE_7.getArticleId());
//
//        assertThat(articleList).containsExactly(ARTICLE_1, ARTICLE_2, ARTICLE_3, ARTICLE_4);
//    }

    private Category buildCategory(Long categoryId){
        return Category.builder().categoryId(categoryId).categoryName("Category " + categoryId).build();
    }

    private Tag buildTag(Long tagId){
        return Tag.builder().tagId(tagId).tagName("Tag " + tagId).build();
    }

    private Article buildArticle(Long articleId, Category category, Set<Tag> tags, LocalDateTime dateArticlePublished){
        return Article.builder().articleId(articleId).articleTitle("Title " + articleId).articleText("Text " + articleId).category(category).tags(tags).dateArticlePublished(dateArticlePublished).build();
    }
}
