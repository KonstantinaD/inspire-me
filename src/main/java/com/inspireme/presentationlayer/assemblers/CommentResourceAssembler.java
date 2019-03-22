package com.inspireme.presentationlayer.assemblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.inspireme.presentationlayer.controllers.CommentController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;
import com.inspireme.domainlayer.Comment;

@Component
public class CommentResourceAssembler implements ResourceAssembler<Comment, Resource<Comment>> {

    @Override
    public Resource<Comment> toResource(Comment comment) {

        return new Resource<>(comment,
                ControllerLinkBuilder.linkTo(methodOn(CommentController.class).getCommentById(comment.getCommentId())).withSelfRel(),
                linkTo(methodOn(CommentController.class).getAllComments()).withRel("comments"));
    }
}

