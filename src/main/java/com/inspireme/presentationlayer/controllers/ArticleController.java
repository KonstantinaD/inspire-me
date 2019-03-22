package com.inspireme.presentationlayer.controllers;

import com.inspireme.domainlayer.Article;
import com.inspireme.domainlayer.User;
import com.inspireme.domainlayer.UserType;
import com.inspireme.infrastructurelayer.ArticleRepository;
import com.inspireme.infrastructurelayer.UserRepository;
import com.inspireme.presentationlayer.assemblers.ArticleResourceAssembler;
import com.inspireme.presentationlayer.notfoundexceptions.ArticleNotFoundException;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/*To wrap your repository with a web layer, you must turn to Spring MVC
An ArticleRepository is injected by constructor into the controller.
All the controller methods return one of Spring HATEOAS’s ResourceSupport subclasses to properly render hypermedia (or an wrapper around such a type).
*/

@RestController  //RestController indicates that the data returned by each method will be written straight into the response body instead of rendering a view template (view model).
public class ArticleController {
    private final ArticleRepository articleRepository;
    private final ArticleResourceAssembler articleAssembler;

    ArticleController(ArticleRepository articleRepository,
                      ArticleResourceAssembler articleAssembler) {
        this.articleRepository = articleRepository;
        this.articleAssembler = articleAssembler;
    }

    //Aggregate root - all articles
//    @GetMapping("/articles")
//    public Resources<Resource<Article>> all() {
//        List<Resource<Article>> articles = articleRepository.findAll().stream()
//                //.map(articleAssembler::toResource)
//                .map(article -> new Resource<>(article,
//                        linkTo(methodOn(ArticleController.class).one(article.getArticleId())).withSelfRel(),
//                        linkTo(methodOn(ArticleController.class).all()).withRel("articles")))
//                .collect(Collectors.toList());
//
//        return new Resources<>(articles,
//                linkTo(methodOn(ArticleController.class).all()).withSelfRel());
//    }


    @GetMapping("/articles")
    public Resources<Resource<Article>> getAllArticles() {
        List<Resource<Article>> articles = articleRepository.findAll().stream()
                .map(articleAssembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(articles,
                linkTo(methodOn(ArticleController.class).getAllArticles()).withSelfRel());
    }

    @GetMapping("/articles/category/{categoryId}")
    public Resources<Resource<Article>> getAllArticlesByCategoryId(@PathVariable Long categoryId) {
        if ((categoryId >= 1) && (categoryId <= 4)) {
            List<Resource<Article>> articles = articleRepository.findByCategoryId(categoryId).stream()
                    .map(articleAssembler::toResource)
                    .collect(Collectors.toList());

            return new Resources<>(articles,
                    linkTo(methodOn(ArticleController.class).getAllArticlesByCategoryId(categoryId)).withSelfRel());
        }

        return null;
    }

    @GetMapping("/articles/{articleId}")
    public Resource<Article> getArticleById(@PathVariable Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow((() -> new ArticleNotFoundException(articleId)));

        return articleAssembler.toResource(article);
    }

    @PostMapping("/articles")
    public ResponseEntity<?> createNewArticle(@RequestBody Article newArticle) throws URISyntaxException {
        if (newArticle.getArticlePublishedBy().getUserId() == 1) {

            Resource<Article> articleResource = articleAssembler.toResource(articleRepository.save(newArticle));

            return ResponseEntity
                    .created(new URI(articleResource.getId().expand().getHref()))  //the italic is the http status
                    .body(articleResource);
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new VndErrors.VndError("Article publisher not allowed", "An article can't be published by user with userId " + newArticle.getArticlePublishedBy().getUserId() + ". Only the Admin user with userId 1 can publish articles."));
    }

    @PutMapping("/articles/{articleId}")
    public ResponseEntity<?> replaceArticle(@RequestBody Article newArticle, @PathVariable Long articleId) throws URISyntaxException {
        if (newArticle.getArticlePublishedBy().getUserId() == 1) {

            Article updatedArticle = articleRepository.findById(articleId)
                    .map(article -> {
                        article.setArticleTitle(newArticle.getArticleTitle());
                        article.setArticleText(newArticle.getArticleText());
                        article.setImageUrl(newArticle.getImageUrl());
                        article.setCategory(newArticle.getCategory());
                        article.setArticlePublishedBy(newArticle.getArticlePublishedBy());
                        //article.setComments(newArticle.getComments());
                        return articleRepository.save(article);
                    })
                    .orElseGet(() -> {
                        //newArticle.setArticleId(articleId);
                        return articleRepository.save(newArticle);
                    });

            Resource<Article> articleResource = articleAssembler.toResource(updatedArticle);

            return ResponseEntity
                    .created(new URI(articleResource.getId().expand().getHref()))
                    .body(articleResource);
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new VndErrors.VndError("Article manipulator not allowed", "An article can't be updated or published by user with userId " + newArticle.getArticlePublishedBy().getUserId() + ". Only the Admin user with userId 1 can update or publish articles."));
    }

    //WE NEED TO PREVENT VISITORS FROM DELETING ARTICLES - MAYBE WITH PERMISSIONS
    @DeleteMapping("articles/{articleId}")
    public ResponseEntity<?> deleteArticle(@PathVariable Long articleId) {

        articleRepository.deleteById(articleId);

        return ResponseEntity.noContent().build();
    }
}

//    /* The Article object built from the save() operation is then turned into its resource-based version - wrapped using the ArticleResourceAssembler into a Resource<Article> object
//    * Since we want a more detailed HTTP response code than 200 OK, we will use Spring MVC’s ResponseEntity wrapper. It has a handy static method created() where we can plug in the resource’s URI.
//     By grabbing the resource you can fetch it’s "self" link via the getId() method call. This method yields a Link which you can turn into a Java URI. To tie things up nicely, you inject the resource itself into the body() method.
//     @RequestBody - the method parameter should be bound to the body of the web request.*/



