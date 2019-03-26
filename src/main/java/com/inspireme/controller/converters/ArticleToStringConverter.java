package com.inspireme.controller.converters;

import com.inspireme.model.Article;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArticleToStringConverter implements Converter<Article, String> {

    @Override
    public String convert(Article article) {
        return String.valueOf(article.getArticleId());
    }
}