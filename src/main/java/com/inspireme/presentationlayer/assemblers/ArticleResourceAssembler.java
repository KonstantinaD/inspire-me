package com.inspireme.presentationlayer.assemblers;

import com.inspireme.domainlayer.Article;
import com.inspireme.presentationlayer.controllers.ArticleController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

//@Component  //this component will be automatically created when the application starts
//public class ArticleResourceAssembler implements ResourceAssembler<Article, Resource<Article>>{
//    @Override
//    public Resource<Article> toResource(Article article) {
//
//        return new Resource<>(article,
//                ControllerLinkBuilder.linkTo(methodOn(ArticleController.class).getArticleById(article.getArticleId())).withSelfRel(),
//                linkTo(methodOn(ArticleController.class).getAllArticlesByCategoryId()
//                linkTo(methodOn(ArticleController.class).getAllArticles()).withRel("articles"));
//    }
//}
