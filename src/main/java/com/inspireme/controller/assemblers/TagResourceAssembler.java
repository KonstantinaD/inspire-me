package com.inspireme.controller.assemblers;

import com.inspireme.controller.TagController;
import com.inspireme.model.Tag;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class TagResourceAssembler implements ResourceAssembler<Tag, Resource<Tag>> {

    @Override
    public Resource<Tag> toResource(Tag tag) {

        return new Resource<>(tag,
                ControllerLinkBuilder.linkTo(methodOn(TagController.class).getTag(tag.getTagId())).withSelfRel(),
                        linkTo(methodOn(TagController.class).getAllTagsByArticle(tag.getArticles().iterator().next())).withRel("tags/article/{articleId}"),
                linkTo(methodOn(TagController.class).getAllTags()).withRel("tags"));
    }
}
