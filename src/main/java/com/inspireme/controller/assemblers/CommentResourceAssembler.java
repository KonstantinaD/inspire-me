package com.inspireme.controller.assemblers;

import com.inspireme.controller.CommentController;
import com.inspireme.model.Comment;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class CommentResourceAssembler implements ResourceAssembler<Comment, Resource<Comment>> {

    @Override
    public Resource<Comment> toResource(Comment comment) {

        return new Resource<>(comment,
                ControllerLinkBuilder.linkTo(methodOn(CommentController.class).getComment(comment.getCommentId())).withSelfRel(),
                linkTo(methodOn(CommentController.class).getCommentsByArticle(comment.getArticle().getArticleId())).withRel("comments/article/{articleId}"),
                linkTo(methodOn(CommentController.class).getAllComments()).withRel("comments"));
    }
}

