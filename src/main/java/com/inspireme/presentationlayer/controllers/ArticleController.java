package com.inspireme.presentationlayer.controllers;

import com.inspireme.domainlayer.Article;
import com.inspireme.domainlayer.User;
import com.inspireme.domainlayer.UserType;
import com.inspireme.infrastructurelayer.ArticleRepository;
import com.inspireme.infrastructurelayer.UserRepository;
import com.inspireme.presentationlayer.notfoundexceptions.ArticleNotFoundException;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/*To wrap your repository with a web layer, you must turn to Spring MVC
An ArticleRepository is injected by constructor into the controller.
All the controller methods return one of Spring HATEOAS’s ResourceSupport subclasses to properly render hypermedia (or an wrapper around such a type).
*/

@RestController  //RestController indicates that the data returned by each method will be written straight into the response body instead of rendering a view template (view model).
public class ArticleController {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    ArticleController(ArticleRepository articleRepository,
                      UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
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


    @PostMapping("/articles")
    public Article newArticle(@RequestBody Article newArticle) {
        return articleRepository.save(newArticle);
    }

    @GetMapping("/articles")
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @GetMapping("/articles/{articleId}")
     public Article getArticleById(@PathVariable Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow((() -> new ArticleNotFoundException(articleId)));
    }

    @GetMapping("/articles/category/{categoryId}")
    public List<Article> getAllArticlesPerCategory(@PathVariable Long categoryId) {
        return articleRepository.findByCategoryId(categoryId);
    }

//    //All articles by category id
//    @GetMapping("/articles/{catId}")
//    public Resources<Resource<Article>> allByCategoryId(@PathVariable Long catId) {
//        List<Resource<Article>> articles = articleRepository.findAllByCategoryId(catId).stream()
//                //.map(articleAssembler::toResource)
//                .map(article -> new Resource<>(article,
//                        linkTo(methodOn(ArticleController.class).one(article.getArticleId())).withSelfRel(),                       linkTo(methodOn(ArticleController.class).allByCategoryId(catId)).withRel("articles" + "/" + "catId"),
//                        linkTo(methodOn(ArticleController.class).all()).withRel("articles")))
//                .collect(Collectors.toList());
//
//        return new Resources<>(articles,
//                linkTo(methodOn(ArticleController.class).allByCategoryId(catId)).withSelfRel());
//    }
//
//    /* The Article object built from the save() operation is then turned into its resource-based version - wrapped using the ArticleResourceAssembler into a Resource<Article> object
//    * Since we want a more detailed HTTP response code than 200 OK, we will use Spring MVC’s ResponseEntity wrapper. It has a handy static method created() where we can plug in the resource’s URI.
//     By grabbing the resource you can fetch it’s "self" link via the getId() method call. This method yields a Link which you can turn into a Java URI. To tie things up nicely, you inject the resource itself into the body() method.
//     @RequestBody - the method parameter should be bound to the body of the web request.*/
//
//    //@PostMapping("/articles")
//    @PostMapping("/articles/{catId}")
//    ResponseEntity<?> newArticle(@RequestBody Article newArticle, Category category) throws URISyntaxException {
//
//        //Before we introduced publishing an article with catID
//        //Resource<Article> resource = articleAssembler.toResource(articleRepository.save(newArticle));
//        Long catId = category.getCategoryId();
//        articleRepository.saveByCategoryId(newArticle, catId);
//        Resource<Article> resource = new Resource<>(newArticle,
//                linkTo(methodOn(ArticleController.class).one(newArticle.getArticleId())).withSelfRel(),                       linkTo(methodOn(ArticleController.class).allByCategoryId(catId)).withRel("articles" + "/" + "catId"),
//                linkTo(methodOn(ArticleController.class).all()).withRel("articles"));
//
//        return ResponseEntity
//                .created(new URI(resource.getId().expand().getHref()))
//                .body(resource);
//    }
//
//    //Single item
//    //@PathVariable - take the id from the URL
//    @GetMapping("/articles/{id}")
//    public Resource<Article> one(@PathVariable Long articleId) {
//
//        Article article = articleRepository.findById(articleId)
//                .orElseThrow(() -> new ArticleNotFoundException(articleId));
//
//        //return articleAssembler.toResource(article);
//        return new Resource<>(article,
//                linkTo(methodOn(ArticleController.class).one(article.getArticleId())).withSelfRel(),                       /*linkTo(methodOn(ArticleController.class).allByCategoryId(catId)).withRel("articles" + "/" + "catId"),*/
//                linkTo(methodOn(ArticleController.class).all()).withRel("articles"));
//    }
//
//    @PutMapping("/articles/{id}")
//    ResponseEntity<?> replaceArticle(@RequestBody Article newArticle, @PathVariable Long articleId) throws URISyntaxException {
//
//        Article updatedArticle = articleRepository.findById(articleId)
//                .map(article -> {
//                    article.setArticleTitle(newArticle.getArticleTitle());
//                    article.setArticleText(newArticle.getArticleText());
//                    article.setImageUrl(newArticle.getImageUrl());
//                    article.setDateArticlePublished(newArticle.getDateArticlePublished());
//                    article.setCategory(newArticle.getCategory());
//                    article.setArticlePublishedBy(newArticle.getArticlePublishedBy());
//                    article.setComments(newArticle.getComments());
//                    return articleRepository.save(article);
//                })
//                .orElseGet(() -> {
//                    newArticle.setArticleId(articleId);
//                    return articleRepository.save(newArticle);
//                });
//
//        //Resource<Article> resource = articleAssembler.toResource(updatedArticle);
//        Resource<Article> resource = new Resource<>(updatedArticle,
//                linkTo(methodOn(ArticleController.class).one(updatedArticle.getArticleId())).withSelfRel(),                       /*linkTo(methodOn(ArticleController.class).allByCategoryId(catId)).withRel("articles" + "/" + "catId"),*/
//                linkTo(methodOn(ArticleController.class).all()).withRel("articles"));
//
//        return ResponseEntity
//                .created(new URI(resource.getId().expand().getHref()))
//                .body(resource);
//    }
//
//    @DeleteMapping("/articles/{id}")
//    ResponseEntity<?> deleteArticle(@PathVariable Long articleId) {
//
//        articleRepository.deleteById(articleId);
//        return ResponseEntity.noContent().build();
//    }
}

