package com.inspireme.service;

import com.inspireme.model.Article;
import com.inspireme.model.Category;
import com.inspireme.model.Tag;

import java.time.LocalDateTime;
import java.util.Set;

public class ArticleServiceImplTestHelper {

    public static Category buildCategory(Long categoryId){
        return Category.builder().categoryId(categoryId).categoryName("Category " + categoryId).build();
    }

    public static Tag buildTag(Long tagId){
        return Tag.builder().tagId(tagId).tagName("Tag " + tagId).build();
    }

    public static Article buildArticle(Long articleId, Category category, Set<Tag> tags,
                                       LocalDateTime dateArticlePublished) {
        return Article.builder().articleId(articleId).articleTitle("Title " + articleId)
                .category(category).tags(tags)
                .dateArticlePublished(dateArticlePublished).build();
    }
}
