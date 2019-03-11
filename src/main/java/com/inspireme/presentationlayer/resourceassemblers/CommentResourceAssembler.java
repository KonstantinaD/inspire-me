package com.inspireme.presentationlayer.resourceassemblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import com.inspireme.domainlayer.Comment;
import com.inspireme.presentationlayer.controllers.CommentController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class CommentResourceAssembler /*implements ResourceAssembler<Comment, Resource<Comment>>*/{
    /*@Override
    public Resource<Comment> toResource(Comment comment) {

        return new Resource<>(comment,
                linkTo(methodOn(CommentController.class).one(comment.getCommentId())).withSelfRel(),
                linkTo(methodOn(CommentController.class).all()).withRel("comments"));
        }*/
    }
