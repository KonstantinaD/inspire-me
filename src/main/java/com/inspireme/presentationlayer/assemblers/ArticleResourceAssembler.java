package com.inspireme.presentationlayer.assemblers;

import com.inspireme.domainlayer.Article;
import com.inspireme.presentationlayer.controllers.ArticleController;
import com.inspireme.presentationlayer.controllers.CommentController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class ArticleResourceAssembler implements ResourceAssembler<Article, Resource<Article>> {

    @Override
    public Resource<Article> toResource(Article article) {

        return new Resource<>(article,
                linkTo(methodOn(ArticleController.class).getArticleById(article.getArticleId())).withSelfRel(),
                linkTo(methodOn(ArticleController.class).getAllArticlesByCategoryId(article.getCategory().getCategoryId())).withRel("category/{categoryId}"),
                linkTo(methodOn(ArticleController.class).getAllArticles()).withRel("articles"));
    }
}
