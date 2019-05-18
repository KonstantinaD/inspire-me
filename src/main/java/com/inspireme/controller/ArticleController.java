package com.inspireme.controller;

import com.inspireme.controller.assemblers.ArticleResourceAssembler;
import com.inspireme.exception.NotFoundException;
import com.inspireme.model.Article;
import com.inspireme.model.Tag;
import com.inspireme.service.ArticleService;
import com.inspireme.service.TagService;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.VndErrors;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleResourceAssembler articleAssembler;
    private final TagService tagService;

    public ArticleController(ArticleService articleService,
                             ArticleResourceAssembler articleAssembler, TagService tagService) {
        this.articleService = articleService;
        this.articleAssembler = articleAssembler;
        this.tagService = tagService;
    }

    @GetMapping
    public Resources<?> getAllArticles() {

        List<Article> articles = articleService.retrieveAllArticles();

        if (!articles.isEmpty()) {
            List<Resource<Article>> articleResources = articles.stream()
                    .map(articleAssembler::toResource)
                    .collect(Collectors.toList());

            return new Resources<>(articleResources,
                    linkTo(methodOn(ArticleController.class).getAllArticles()).withSelfRel());
        }

        return new Resources<>(Arrays.asList(getEmptyListArticleWrapper()),
                linkTo(methodOn(ArticleController.class).getAllArticles()).withSelfRel());
    }

    @GetMapping("/category/{categoryId}")
    public Resources<?> getArticlesByCategory(@PathVariable Long categoryId) {

        List<Article> articles = articleService.retrieveAllArticlesPerCategory(categoryId);

        if (!articles.isEmpty()) {
            List<Resource<Article>> articleResources = articles.stream()
                    .map(articleAssembler::toResource)
                    .collect(Collectors.toList());

            return new Resources<>(articleResources,
                    linkTo(methodOn(ArticleController.class).getArticlesByCategory(categoryId)).withSelfRel());
        }

        return new Resources<>(Arrays.asList(getEmptyListArticleWrapper()),
                linkTo(methodOn(ArticleController.class).getArticlesByCategory(categoryId)).withSelfRel());
    }

    @GetMapping("/tags/{tagId}")
    public Resources<?> getArticlesByTag(@PathVariable Long tagId) {

        List<Article> articles = articleService.retrieveAllArticlesPerTag(tagId);

        if (!articles.isEmpty()) {
            List<Resource<Article>> articleResources = articles.stream()
                    .map(articleAssembler::toResource)
                    .collect(Collectors.toList());

            return new Resources<>(articleResources,
                    linkTo(methodOn(ArticleController.class).getArticlesByTag(tagId)).withSelfRel());
        }

        return new Resources<>(Arrays.asList(getEmptyListArticleWrapper()),
                linkTo(methodOn(ArticleController.class).getArticlesByTag(tagId)).withSelfRel());
    }

    @GetMapping("/relatedArticles/{articleId}")
    public Resources<?> getRelatedArticles(@PathVariable Long articleId) {

        List<Article> relatedArticles = articleService.retrieveRelatedArticles(articleId);

        if (!relatedArticles.isEmpty()) {
            List<Resource<Article>> relatedArticleResources = relatedArticles.stream()
                    .map(articleAssembler::toResource)
                    .collect(Collectors.toList());

            return new Resources<>(relatedArticleResources,
                    linkTo(methodOn(ArticleController.class).getRelatedArticles(articleId)).withSelfRel());
        }

        return new Resources<>(Arrays.asList(getEmptyListArticleWrapper()),
                linkTo(methodOn(ArticleController.class).getRelatedArticles(articleId)).withSelfRel());
    }

    @GetMapping("/{articleId}")
    public Resource<Article> getArticle(@PathVariable Long articleId) {
        return articleAssembler.toResource(articleService.retrieveArticle(articleId));
    }

    @PostMapping
    public ResponseEntity<?> createNewArticle(@RequestBody @Valid Article newArticle) throws URISyntaxException {

        if (newArticle.getArticlePublishedBy().getUserId() == 1) {

            Resource<Article> articleResource = articleAssembler.toResource(articleService.saveArticle(newArticle));

            return ResponseEntity
                    .created(new URI(articleResource.getId().expand().getHref()))  //the italic is the http status
                    .body(articleResource);
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new VndErrors.VndError("Article Publisher Not Allowed", "An article can't be published by user with user id " + newArticle.getArticlePublishedBy().getUserId() + ". Only the Admin user with user id 1 can publish articles."));
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<?> editArticle(@RequestBody @Valid Article newArticle, @PathVariable Long articleId) throws URISyntaxException {

//        if (newArticle.getArticlePublishedBy().getUserId() == 1) { //PERMISSIONS - secure endpoint better
        Article articleToUpdate = articleService.updateArticle(newArticle, articleId);
        Resource<Article> articleResource = articleAssembler.toResource(articleToUpdate);

        return ResponseEntity
                    .created(new URI(articleResource.getId().expand().getHref()))
                    .body(articleResource);
        }
//        return ResponseEntity
//                .status(HttpStatus.FORBIDDEN)
//                .body(new VndErrors.VndError("Article Manipulator Not Allowed", "An article can't be edited or published by user with user id " + newArticle.getArticlePublishedBy().getUserId() + ". Only the Admin user with user id 1 can edit or publish articles."));
//    }


    //WE NEED TO PREVENT VISITORS FROM DELETING ARTICLES - MAYBE WITH PERMISSIONS
    @DeleteMapping("/{articleId}")
    public ResponseEntity<?> deleteArticle(@PathVariable Long articleId) {

        articleService.deleteArticle(articleId);

        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{articleId}/tags/{tagId}")
    public ResponseEntity addTagsToArticle(@PathVariable Long articleId, @PathVariable/*@RequestBody /*List<*/Long/*>*/ tagId) throws URISyntaxException {

        Article article = articleService.retrieveArticle(articleId);

//        tagIds.stream()    //add list of tags - DECIDE
//                .map(tagService::retrieveTag)
//                .forEach(tag -> article.getTags().add(tag));

        article.getTags().add(tagService.retrieveTag(tagId));

        articleService.saveArticle(article);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{articleId}/tags/{tagId}")
    public ResponseEntity deleteTagFromArticle(@PathVariable Long articleId, @PathVariable Long tagId) {

        Article article = articleService.retrieveArticle(articleId);

        boolean tagDeleted = article.getTags().remove(tagService.retrieveTag(tagId));

        if (!tagDeleted){
            throw new NotFoundException(tagId, Tag.class);
        }

        articleService.saveArticle(article);

        return ResponseEntity.noContent().build();
    }

    private EmbeddedWrapper getEmptyListArticleWrapper(){
        return new EmbeddedWrappers(false).emptyCollectionOf(Article.class);
    }
}





