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
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
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
//@RequestMapping(path = "/articles")
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

    @GetMapping("/articles")
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

    @GetMapping("/articles/category/{categoryId}")
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

    @GetMapping("/articles/tags/{tagId}")
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

    @GetMapping("/articles/relatedArticles/{articleId}")
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

    @GetMapping("/articles/{articleId}")
    public Resource<Article> getArticle(@PathVariable Long articleId) {
        return articleAssembler.toResource(articleService.retrieveArticle(articleId));
    }

    @PostMapping("/articles")
    public ResponseEntity<?> createNewArticle(@RequestBody @Valid Article newArticle) throws URISyntaxException {

//        if (newArticle.getArticlePublishedBy().getUserId() == 1) { //disabled due to front end

            Resource<Article> articleResource = articleAssembler.toResource(articleService.saveArticle(newArticle));

            return ResponseEntity
                    .created(new URI(articleResource.getId().expand().getHref()))
                    .body(articleResource);
//        }
//
//        return ResponseEntity
//                .status(HttpStatus.FORBIDDEN)
//                .body(new VndErrors.VndError("Article Publisher Not Allowed", "An article can't be published by user with user id " + newArticle.getArticlePublishedBy().getUserId() + ". Only the Admin user with user id 1 can publish articles."));
    }

    @PutMapping("/articles/{articleId}")
    public ResponseEntity<?> editArticle(@RequestBody @Valid Article newArticle, @PathVariable Long articleId) throws URISyntaxException {

//        if (newArticle.getArticlePublishedBy().getUserId() == 1) { //PERMISSIONS - secure endpoint better
        Article updatedArticle = articleService.updateArticle(newArticle, articleId);

        Resource<Article> articleResource = articleAssembler.toResource(updatedArticle);

        return ResponseEntity
                    .created(new URI(articleResource.getId().expand().getHref()))
                    .body(articleResource);
        }
//        return ResponseEntity
//                .status(HttpStatus.FORBIDDEN)
//                .body(new VndErrors.VndError("Article Manipulator Not Allowed", "An article can't be edited or published by user with user id " + newArticle.getArticlePublishedBy().getUserId() + ". Only the Admin user with user id 1 can edit or publish articles."));
//    }


    //WE NEED TO PREVENT VISITORS FROM DELETING ARTICLES - MAYBE WITH PERMISSIONS
    @DeleteMapping("/articles/{articleId}")
    public ResponseEntity<?> removeArticle(@PathVariable Long articleId) {

        articleService.deleteArticle(articleId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("tags/article/{articleId}")
    public Resources<?> getTagsByArticle(@PathVariable Long articleId) {

        List<Tag> tags = articleService.retrieveAllTagsPerArticle(articleId);

        if (!tags.isEmpty()) {
            List<Resource<Tag>> tagResources = tags.stream()
                    .map(tag -> new Resource<>(tag,
                            ControllerLinkBuilder.linkTo(methodOn(TagController.class).getTag(tag.getTagId())).withSelfRel(),
                            linkTo(methodOn(ArticleController.class).getTagsByArticle(articleId)).withRel("tags/article/{articleId}"),
                            linkTo(methodOn(TagController.class).getAllTags()).withRel("tags")))
                    .collect(Collectors.toList());

            return new Resources<>(tagResources,
                    linkTo(methodOn(ArticleController.class).getTagsByArticle(articleId)).withSelfRel());
        }

        return new Resources<>(Arrays.asList(TagController.getEmptyListTagWrapper()),
                linkTo(methodOn(ArticleController.class).getTagsByArticle(articleId)).withSelfRel());
    }

    @PostMapping("/articles/{articleId}/tags")
    public ResponseEntity addTagsToArticle(@PathVariable Long articleId, @RequestBody List<Long> tagIds) throws URISyntaxException {

        Article article = articleService.retrieveArticle(articleId);

        tagIds.stream()
                .map(tagService::retrieveTag)
                .forEach(tag -> article.getTags().add(tag));

        articleService.saveArticle(article);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/articles/{articleId}/tags/{tagId}")
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





