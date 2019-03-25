package com.inspireme.presentationlayer.controllers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import com.inspireme.presentationlayer.assemblers.CommentResourceAssembler;
import com.inspireme.servicelayer.services.CommentService;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.inspireme.domainlayer.Comment;
import com.inspireme.infrastructurelayer.repositories.CommentRepository;
import com.inspireme.presentationlayer.notfoundexceptions.CommentNotFoundException;

@RestController
public class CommentController {

    private final CommentService commentService;
    private final CommentResourceAssembler commentAssembler;

    public CommentController(CommentService commentService, CommentResourceAssembler commentAssembler) {
        this.commentService = commentService;
        this.commentAssembler = commentAssembler;
    }

    @GetMapping("/comments")
    public Resources<Resource<Comment>> getAllComments() {
        List<Resource<Comment>> comments = commentService.retrieveAllComments().stream()
                .map(commentAssembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(comments,
                linkTo(methodOn(CommentController.class).getAllComments()).withSelfRel());
    }

    @GetMapping("/comments/article/{articleId}")
    public Resources<Resource<Comment>> getAllCommentsByArticleId(@PathVariable Long articleId) {
        if (!commentService.retrieveAllCommentsPerArticle(articleId).isEmpty()) {
            List<Resource<Comment>> comments = commentService.retrieveAllCommentsPerArticle(articleId).stream()
                    .map(commentAssembler::toResource)
                    .collect(Collectors.toList());

            return new Resources<>(comments,
                    linkTo(methodOn(CommentController.class).getAllCommentsByArticleId(articleId)).withSelfRel());
        }

        return null;
    }

    @GetMapping("/comments/{commentId}")
    public Resource<Comment> getCommentById(@PathVariable Long commentId) {

        Comment comment = commentService.retrieveComment(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        return commentAssembler.toResource(comment);
    }

    @PostMapping("/comments")
    public ResponseEntity<?> createNewComment(@RequestBody Comment newComment) throws URISyntaxException {
        Resource<Comment> resource = commentAssembler.toResource(commentService.saveComment(newComment));

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    //PERMISSIONS - ONLY THE PUBLISHER OF THE COMMENT CAN EDIT/DELETE IT, THE BELOW ONLY PREVENTS THE COMMENT PUBLISHER ID FROM CHANGING
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<?> replaceComment(@RequestBody Comment newComment, @PathVariable Long commentId) throws URISyntaxException {
            Comment updatedComment = commentService.retrieveComment(commentId)
                    .map(comment -> {
                        comment.setCommentText(newComment.getCommentText());
                        comment.setArticle(newComment.getArticle());
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
    @DeleteMapping("comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {

           commentService.deleteComment(commentId);

           return ResponseEntity.noContent().build();
    }
}
