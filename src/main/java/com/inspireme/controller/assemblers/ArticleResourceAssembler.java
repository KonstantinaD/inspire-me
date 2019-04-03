package com.inspireme.controller.assemblers;

import com.inspireme.controller.ArticleController;
import com.inspireme.model.Article;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class ArticleResourceAssembler implements ResourceAssembler<Article, Resource<Article>> {

    @Override
    public Resource<Article> toResource(Article article) {

        return new Resource<>(article,
                linkTo(methodOn(ArticleController.class).getArticle(article.getArticleId())).withSelfRel(),
                linkTo(methodOn(ArticleController.class).getArticlesByCategory(article.getCategory())).withRel("articles/category/{categoryId}"),
                linkTo(methodOn(ArticleController.class).getRelatedArticles(article)).withRel("articles/relatedArticles/{articleId}"),
                linkTo(methodOn(ArticleController.class).getAllArticles()).withRel("articles"));
    }
}
