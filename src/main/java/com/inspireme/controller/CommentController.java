package com.inspireme.controller;

import com.inspireme.controller.assemblers.CommentResourceAssembler;
import com.inspireme.exception.NotFoundException;
import com.inspireme.model.Article;
import com.inspireme.model.Comment;
import com.inspireme.service.CommentService;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
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
    public Resources<?> getAllComments() {

        List<Comment> comments = commentService.retrieveAllComments();

        if (!comments.isEmpty()) {
            List<Resource<Comment>> commentResources = comments.stream()
                    .map(commentAssembler::toResource)
                    .collect(Collectors.toList());

            return new Resources<>(commentResources,
                    linkTo(methodOn(CommentController.class).getAllComments()).withSelfRel());
        }

        return new Resources<>(Arrays.asList(getEmptyListCommentWrapper()),
                linkTo(methodOn(CommentController.class).getAllComments()).withSelfRel());
    }

    @GetMapping("/article/{articleId}")
    public Resources<?> getCommentsByArticle(@PathVariable Long articleId) {

        List<Comment> comments = commentService.retrieveAllCommentsPerArticle(articleId);

        if (!comments.isEmpty()) {
            List<Resource<Comment>> commentResources = comments.stream()
                    .map(commentAssembler::toResource)
                    .collect(Collectors.toList());

            return new Resources<>(commentResources,
                    linkTo(methodOn(CommentController.class).getCommentsByArticle(articleId)).withSelfRel());
        }

        return new Resources<>(Arrays.asList(getEmptyListCommentWrapper()), linkTo(methodOn(CommentController.class).getCommentsByArticle(articleId)).withSelfRel());
    }

    @GetMapping("/{commentId}")
    public Resource<Comment> getComment(@PathVariable Long commentId) {

        return commentAssembler.toResource(commentService.retrieveComment(commentId));
    }

    @PostMapping
    public ResponseEntity<?> createNewComment(@RequestBody @Valid Comment newComment) throws URISyntaxException {

        Resource<Comment> commentResource = commentAssembler.toResource(commentService.saveComment(newComment));

        return ResponseEntity
                .created(new URI(commentResource.getId().expand().getHref()))
                .body(commentResource);
    }

    //PERMISSIONS - ONLY THE PUBLISHER OF THE COMMENT CAN EDIT/DELETE IT, THE BELOW ONLY PREVENTS THE COMMENT PUBLISHER ID FROM CHANGING
    @PutMapping("/{commentId}")
    public ResponseEntity<?> editComment(@RequestBody @Valid Comment newComment, @PathVariable Long commentId) throws URISyntaxException {

        Comment updatedComment = commentService.updateComment(newComment, commentId);

        Resource<Comment> commentResource = commentAssembler.toResource(updatedComment);

        return ResponseEntity
                    .created(new URI(commentResource.getId().expand().getHref()))
                    .body(commentResource);
    }

    //WE NEED TO ENSURE THE COMMENT CAN BE DELETED ONLY BY THE PERSON WHO PUBLISHED IT(THE ADMIN SHOULD BE ABLE TO DELETE ALL COMMENTS) - PERMISSIONS
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> removeComment(@PathVariable Long commentId) {

           commentService.deleteComment(commentId);

           return ResponseEntity.noContent().build();
    }

    private EmbeddedWrapper getEmptyListCommentWrapper(){
        return new EmbeddedWrappers(false).emptyCollectionOf(Comment.class);
    }
}
