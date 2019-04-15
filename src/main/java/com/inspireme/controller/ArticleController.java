package com.inspireme.controller;

import com.inspireme.controller.assemblers.ArticleResourceAssembler;
import com.inspireme.exception.ArticleNotFoundException;
import com.inspireme.model.Article;
import com.inspireme.model.Category;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Iterator;
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

    @GetMapping("/category/{category}")
    public Resources<Resource<Article>> getArticlesByCategory(@PathVariable Category category) {
        if (!articleService.retrieveAllArticlesPerCategory(category).isEmpty()) {
            List<Resource<Article>> articles = articleService.retrieveAllArticlesPerCategory(category).stream()
                    .map(articleAssembler::toResource)
                    .collect(Collectors.toList());

            return new Resources<>(articles,
                    linkTo(methodOn(ArticleController.class).getArticlesByCategory(category)).withSelfRel());
        }
        return null;
    }

    @GetMapping("/tags/{tag}") //To DO: implement when not existing tag is called - return TAG NOT FOUND - AND an empty array for a tag which contains no articles
    public Resources<Resource<Article>> getArticlesByTag(@PathVariable Tag tag) {
        if (!articleService.retrieveAllArticlesPerTag(tag).isEmpty()) {
            List<Resource<Article>> articles = articleService.retrieveAllArticlesPerTag(tag).stream()
                    .map(articleAssembler::toResource)
                    .collect(Collectors.toList());

            return new Resources<>(articles,
                    linkTo(methodOn(ArticleController.class).getArticlesByTag(tag)).withSelfRel());
        }
        return null;
    }

    @GetMapping("/relatedArticles/{articleId}")
      public Resources<Resource<Article>> getRelatedArticles(@PathVariable Long articleId) {
        if (!articleService.retrieveRelatedArticles(articleId).isEmpty()) {
            List<Resource<Article>> relatedArticles = articleService.retrieveRelatedArticles(articleId).stream()
                    .map(articleAssembler::toResource)
                    .collect(Collectors.toList());

            return new Resources<>(relatedArticles,
                    linkTo(methodOn(ArticleController.class).getRelatedArticles(articleId)).withSelfRel());
        }
        return null;
    }

    @GetMapping("/{articleId}")
    public Resource<Article> getArticle(@PathVariable Long articleId) {
        Article article = articleService.retrieveArticle(articleId)
                .orElseThrow((() -> new ArticleNotFoundException(articleId)));
        return articleAssembler.toResource(article);
    }

    @PostMapping
    public ResponseEntity<?> createNewArticle(@RequestBody Article newArticle) throws URISyntaxException {
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
        if (newArticle.getArticlePublishedBy().getUserId() == 1) {

            Article updatedArticle = articleService.retrieveArticle(articleId)
                    .map(article-> {
                        article.setArticleTitle(newArticle.getArticleTitle());
                        article.setArticleText(newArticle.getArticleText());
                        article.setImageUrl(newArticle.getImageUrl());
                        article.setCategory(newArticle.getCategory());
                        //article.setArticlePublishedBy(newArticle.getArticlePublishedBy());
                        return articleService.saveArticle(article);
                    })
                    .orElseGet(() -> {
                        return articleService.saveArticle(newArticle);
                    });

            Resource<Article> articleResource = articleAssembler.toResource(updatedArticle);

            return ResponseEntity
                    .created(new URI(articleResource.getId().expand().getHref()))
                    .body(articleResource);
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new VndErrors.VndError("Article Manipulator Not Allowed", "An article can't be edited or published by user with user id " + newArticle.getArticlePublishedBy().getUserId() + ". Only the Admin user with user id 1 can edit or publish articles."));
    }

    //WE NEED TO PREVENT VISITORS FROM DELETING ARTICLES - MAYBE WITH PERMISSIONS
    @DeleteMapping("/{article}")
    public ResponseEntity<?> deleteArticle(@PathVariable Article article) {

        articleService.deleteArticle(article);

        return ResponseEntity.noContent().build();
    }

//    @DeleteMapping("/{article}/{tag}")
//    public void deleteTagFromArticle(@PathVariable Article article, @PathVariable Tag tag) {
//
//        tagService.deleteTagPerArticle(article, tag);
//
//        //return ResponseEntity.noContent().build();
//    }
}

//    /* The Article object built from the save() operation is then turned into its resource-based version - wrapped using the ArticleResourceAssembler into a Resource<Article> object
//    * Since we want a more detailed HTTP response code than 200 OK, we will use Spring MVC’s ResponseEntity wrapper. It has a handy static method created() where we can plug in the resource’s URI.
//     By grabbing the resource you can fetch it’s "self" link via the getId() method call. This method yields a Link which you can turn into a Java URI. To tie things up nicely, you inject the resource itself into the body() method.
//     @RequestBody - the method parameter should be bound to the body of the web request.*/



