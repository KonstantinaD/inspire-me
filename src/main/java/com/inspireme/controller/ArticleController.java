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

/*To wrap your repository with a web layer, you must turn to Spring MVC
An ArticleRepository is injected by constructor into the controller.
All the controller methods return one of Spring HATEOAS’s ResourceSupport subclasses to properly render hypermedia (or an wrapper around such a type).
*/

@RestController  //RestController indicates that the data returned by each method will be written straight into the response body instead of rendering a view template (view model).
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
    public Resources<Resource<Article>> getAllArticles() {

        if (!articleService.retrieveAllArticles().isEmpty()) {
            List<Resource<Article>> articles = articleService.retrieveAllArticles().stream()
                    .map(articleAssembler::toResource)
                    .collect(Collectors.toList());

            return new Resources<>(articles,
                    linkTo(methodOn(ArticleController.class).getAllArticles()).withSelfRel());
        }

//        EmbeddedWrappers wrappers = new EmbeddedWrappers(false);
//        EmbeddedWrapper wrapper = wrappers.emptyCollectionOf(Article.class);
//        Resources<Resource<Article>> resources = new Resources<>(Arrays.asList(wrapper));
//        return resources;

        return null;
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
    public ResponseEntity<?> replaceArticle(@RequestBody Article newArticle, @PathVariable Long articleId) throws URISyntaxException {

        Article updatedArticle = articleService.replaceArticle(articleId, newArticle);
        Resource<Article> articleResource = articleAssembler.toResource(updatedArticle);

        return ResponseEntity
                .created(new URI(articleResource.getId().expand().getHref()))
                .body(articleResource);

//        return ResponseEntity
//                .status(HttpStatus.FORBIDDEN)
//                .body(new VndErrors.VndError("Article Manipulator Not Allowed", "An article can't be edited or published by user with user id " + newArticle.getArticlePublishedBy().getUserId() + ". Only the Admin user with user id 1 can edit or publish articles."));
    }

    //WE NEED TO PREVENT VISITORS FROM DELETING ARTICLES - MAYBE WITH PERMISSIONS
    @DeleteMapping("/{articleId}")
    public ResponseEntity<?> deleteArticle(@PathVariable Long articleId) {

        articleService.deleteArticle(articleId);

        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{article}/tags")
    public ResponseEntity addTagsToArticle(@PathVariable Article article, @RequestBody List<Long> tagIds) throws URISyntaxException {

        tagIds.stream()
                .map(tagService::retrieveTag)
                .forEach(tag -> article.getTags().add(tag));

        articleService.saveArticle(article);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{article}/tags/{tag}")
    public ResponseEntity deleteTagFromArticle(@PathVariable Article article, @PathVariable Tag tag) {

        boolean deleted = article.getTags().remove(tag);

        if (!deleted){
            throw new NotFoundException(tag.getTagId(), Tag.class);
        }

        articleService.saveArticle(article);

        return ResponseEntity.noContent().build();
    }

    private EmbeddedWrapper getEmptyListArticleWrapper(){
        return new EmbeddedWrappers(false).emptyCollectionOf(Article.class);
    }
}

//    /* The Article object built from the save() operation is then turned into its resource-based version - wrapped using the ArticleResourceAssembler into a Resource<Article> object
//    * Since we want a more detailed HTTP response code than 200 OK, we will use Spring MVC’s ResponseEntity wrapper. It has a handy static method created() where we can plug in the resource’s URI.
//     By grabbing the resource you can fetch it’s "self" link via the getId() method call. This method yields a Link which you can turn into a Java URI. To tie things up nicely, you inject the resource itself into the body() method.
//     @RequestBody - the method parameter should be bound to the body of the web request.*/



