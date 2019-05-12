package com.inspireme.controller;

import com.inspireme.controller.assemblers.TagResourceAssembler;
import com.inspireme.model.Article;
import com.inspireme.model.Tag;
import com.inspireme.service.TagService;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/tags")
public class TagController {

    private final TagService tagService;
    private final TagResourceAssembler tagAssembler;

    public TagController(TagService tagService, TagResourceAssembler tagAssembler) {
        this.tagService = tagService;
        this.tagAssembler = tagAssembler;
    }

    @GetMapping
    public Resources<Resource<Tag>> getAllTags() {
        if (!tagService.retrieveAllTags().isEmpty()) {
            List<Resource<Tag>> tags = tagService.retrieveAllTags().stream()
                    .map(tag -> new Resource<>(tag,
                            linkTo(methodOn(TagController.class).getTag(tag.getTagId())).withSelfRel(),
                            linkTo(methodOn(TagController.class).getAllTags()).withRel("tags")))
                    .collect(Collectors.toList());

            return new Resources<>(tags,
                    linkTo(methodOn(TagController.class).getAllTags()).withSelfRel());
        }

        return null;
    }

    @GetMapping("/article/{article}")
    public Resources<Resource<Tag>> getAllTagsByArticle(@PathVariable Article article) {
        if (!tagService.retrieveAllTagsPerArticle(article).isEmpty()) {
            List<Resource<Tag>> tags = tagService.retrieveAllTagsPerArticle(article).stream()
                    .map(tagAssembler::toResource)
                    .collect(Collectors.toList());

            return new Resources<>(tags,
                    linkTo(methodOn(TagController.class).getAllTagsByArticle(article)).withSelfRel());
        }

        return null;
    }

    @GetMapping("/{tagId}")
    public Resource<Tag> getTag(@PathVariable Long tagId) {

        Tag tag = tagService.retrieveTag(tagId);

        return new Resource<>(tag,
                linkTo(methodOn(TagController.class).getTag(tag.getTagId())).withSelfRel(),
                linkTo(methodOn(TagController.class).getAllTags()).withRel("tags"));
    }

//    @DeleteMapping("/{tag}")
//    public ResponseEntity<?> deleteTagFromArticle(@PathVariable Tag tag, @RequestBody Set<Article> articles) {
//        if (tagService.retrieveTag(tag.getTagId()/*tagId*/).isPresent()) {
//            //List<Article> articlesWithTag = articleService.retrieveAllArticlesPerTag(tagService.retrieveTag(tagId).get());
//            for (Article article : articles/*articlesWithTag*//*tag.getArticles()*/) {
//                //Article articleToLoseTag = articles.//articleService.retrieveArticle(article.getArticleId()).get();
//                article /*ToLoseTag*/.getTags().remove(tag);//(tagService.retrieveTag(tagId));
//                articleService.saveArticle(article /*ToLoseTag*/);
//            }
//            return null;
//        }
//
//        return ResponseEntity
//                .status(HttpStatus.NOT_FOUND)
//                .body(new VndErrors.VndError("Tag Not Found", "The tag with tag id " + tag.getTagId() + " doesn't exist."));
//    }
}


