package com.inspireme.presentationlayer.controllers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import com.inspireme.domainlayer.Comment;

@Component
public class CommentResourceAssembler implements ResourceAssembler<Comment, Resource<Comment>> {

    @Override
    public Resource<Comment> toResource(Comment comment) {

        return new Resource<>(comment,
                linkTo(methodOn(CommentController.class).one(comment.getCommentId())).withSelfRel(),
                linkTo(methodOn(CommentController.class).all()).withRel("comments"));
    }
}

