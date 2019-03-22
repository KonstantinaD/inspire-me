package com.inspireme.presentationlayer.controllers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import com.inspireme.presentationlayer.assemblers.CommentResourceAssembler;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.inspireme.domainlayer.Comment;
import com.inspireme.infrastructurelayer.CommentRepository;
import com.inspireme.presentationlayer.notfoundexceptions.CommentNotFoundException;

@RestController
public class CommentController {

    private final CommentRepository commentRepository;
    private final CommentResourceAssembler commentAssembler;

    CommentController(CommentRepository commentRepository, CommentResourceAssembler commentAssembler) {
        this.commentRepository = commentRepository;
        this.commentAssembler = commentAssembler;
    }

    @GetMapping("/comments")
    public Resources<Resource<Comment>> getAllComments() {
        List<Resource<Comment>> comments = commentRepository.findAll().stream()
                .map(commentAssembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(comments,
                linkTo(methodOn(CommentController.class).getAllComments()).withSelfRel());
    }

    @GetMapping("/comments/{commentId}")
    public Resource<Comment> getCommentById(@PathVariable Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        return commentAssembler.toResource(comment);
    }

    @PostMapping("/comments")
    public ResponseEntity<?> createNewComment(@RequestBody Comment newComment) throws URISyntaxException {
        Resource<Comment> resource = commentAssembler.toResource(commentRepository.save(newComment));

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<?> replaceComment(@RequestBody Comment newComment, @PathVariable Long commentId) throws URISyntaxException {
            Comment updatedComment = commentRepository.findById(commentId)
                    .map(comment -> {
                        comment.setCommentText(newComment.getCommentText());
                        comment.setArticle(newComment.getArticle());
                        comment.setCommentPublishedBy(newComment.getCommentPublishedBy());
                        return commentRepository.save(comment);
                    })
                    .orElseGet(() -> {
                        return commentRepository.save(newComment);
                    });

            Resource<Comment> commentResource = commentAssembler.toResource(updatedComment);

            return ResponseEntity
                    .created(new URI(commentResource.getId().expand().getHref()))
                    .body(commentResource);
    }

    @DeleteMapping("comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {

           commentRepository.deleteById(commentId);

           return ResponseEntity.noContent().build();
    }
}
