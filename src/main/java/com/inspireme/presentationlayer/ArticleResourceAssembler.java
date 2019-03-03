package com.inspireme.presentationlayer;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import com.inspireme.domainlayer.Article;
import com.inspireme.presentationlayer.controllers.ArticleController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/*This interface has one method: toResource(). It is based on converting a non-resource object (Article) into a resource-based object (Resource<Article>).
 * Resource<T> is a generic container from Spring HATEOAS that includes not only the data but a collection of links. One of Spring HATEOAS’s core types is Link. It includes a URI and a rel (relation).
 * HAL is a lightweight mediatype that allows encoding not just data but also hypermedia controls, alerting consumers to other parts of the API they can navigate toward. In this case, there is a "self" link (kind of like a this statement in code) along with a link back to the aggregate root.
 * What is the point of adding all these links? It makes it possible to evolve REST services over time. Existing links can be maintained while new links are added in the future. Newer clients may take advantage of the new links, while legacy clients can sustain themselves on the old links. This is especially helpful if services get relocated and moved around. As long as the link structure is maintained, clients can STILL find and interact with things.
 */
@Component  ////this component will be automatically created when the application starts
public class ArticleResourceAssembler implements ResourceAssembler<Article, Resource<Article>>{
    @Override
    public Resource<Article> toResource(Article article) {
     /*linkTo(methodOn(ArticleController.class).one(id)).withSelfRel() asks that Spring HATEOAS build a link to the ArticleController's one() method, and flag it as a self link.
        linkTo(methodOn(ArticleController.class).all()).withRel("articles") asks Spring HATEOAS to build a link to the aggregate root, all(), and call it "articles".*/

        return new Resource<>(article,
                linkTo(methodOn(ArticleController.class).one(article.getArticleId())).withSelfRel(),
                linkTo(methodOn(ArticleController.class).all()).withRel("articles"));
    }
}
