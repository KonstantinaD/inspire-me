package com.inspireme.controller;

import com.inspireme.controller.assemblers.CommentResourceAssembler;
import com.inspireme.exception.NotFoundException;
import com.inspireme.model.Article;
import com.inspireme.model.Comment;
import com.inspireme.service.CommentService;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/comments")
public class CommentController {

    private final CommentService commentService;
    private final CommentResourceAssembler commentAssembler;

    public CommentController(CommentService commentService, CommentResourceAssembler commentAssembler) {
        this.commentService = commentService;
        this.commentAssembler = commentAssembler;
    }

    @GetMapping
    public Resources<Resource<Comment>> getAllComments() {
        if (!commentService.retrieveAllComments().isEmpty()) {
            List<Resource<Comment>> comments = commentService.retrieveAllComments().stream()
                    .map(commentAssembler::toResource)
                    .collect(Collectors.toList());

            return new Resources<>(comments,
                    linkTo(methodOn(CommentController.class).getAllComments()).withSelfRel());
        }

        return null;
    }

    @GetMapping("/article/{article}")
    public Resources<Resource<Comment>> getAllCommentsByArticle(@PathVariable Article article) {
        if (!commentService.retrieveAllCommentsPerArticle(article).isEmpty()) {
            List<Resource<Comment>> comments = commentService.retrieveAllCommentsPerArticle(article).stream()
                    .map(commentAssembler::toResource)
                    .collect(Collectors.toList());

            return new Resources<>(comments,
                    linkTo(methodOn(CommentController.class).getAllCommentsByArticle(article)).withSelfRel());
        }

        return null;
    }

    @GetMapping("/{commentId}")
    public Resource<Comment> getComment(@PathVariable Long commentId) {

        Comment comment = commentService.retrieveComment(commentId)
                .orElseThrow(() -> new NotFoundException(commentId, Comment.class));

        return commentAssembler.toResource(comment);
    }

    @PostMapping
    public ResponseEntity<?> createNewComment(@RequestBody Comment newComment) throws URISyntaxException {
        Resource<Comment> resource = commentAssembler.toResource(commentService.saveComment(newComment));

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    //PERMISSIONS - ONLY THE PUBLISHER OF THE COMMENT CAN EDIT/DELETE IT, THE BELOW ONLY PREVENTS THE COMMENT PUBLISHER ID FROM CHANGING
    @PutMapping("/{commentId}")
    public ResponseEntity<?> replaceComment(@RequestBody Comment newComment, @PathVariable Long commentId) throws URISyntaxException {
            Comment updatedComment = commentService.retrieveComment(commentId)
                    .map(comment -> {
                        comment.setCommentText(newComment.getCommentText());
                        comment.setArticle(newComment.getArticle());
                        //ONLY THE SAME USER CAN UPDATE THEIR OWN COMMENT - below is deactivated
                        //comment.setCommentPublishedBy(newComment.getCommentPublishedBy());
                        return commentService.saveComment(comment);
                    })
                    .orElseGet(() -> {
                        return commentService.saveComment(newComment);
                    });

            Resource<Comment> commentResource = commentAssembler.toResource(updatedComment);

            return ResponseEntity
                    .created(new URI(commentResource.getId().expand().getHref()))
                    .body(commentResource);
    }

    //WE NEED TO ENSURE THE COMMENT CAN BE DELETED ONLY BY THE PERSON WHO PUBLISHED IT(THE ADMIN SHOULD BE ABLE TO DELETE ALL COMMENTS) - PERMISSIONS
    @DeleteMapping("/{comment}")
    public ResponseEntity<?> deleteComment(@PathVariable Comment comment) {

           commentService.deleteComment(comment);

           return ResponseEntity.noContent().build();
    }
}
