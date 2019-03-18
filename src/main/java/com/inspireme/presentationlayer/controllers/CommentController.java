package com.inspireme.presentationlayer.controllers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.inspireme.domainlayer.Comment;
import com.inspireme.infrastructurelayer.CommentRepository;
import com.inspireme.presentationlayer.notfoundexceptions.CommentNotFoundException;

@RestController
public class CommentController {

    private final CommentRepository repository;
    private final CommentResourceAssembler assembler;

    CommentController(CommentRepository repository,  CommentResourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/comments")
    public Resources<Resource<Comment>> all() {
        List<Resource<Comment>> comments = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(comments,
                linkTo(methodOn(CommentController.class).all()).withSelfRel());
    }

    @GetMapping("/comments/{commentId}")
    public Resource<Comment> one(@PathVariable Long commentId) {

        Comment comment = repository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        return assembler.toResource(comment);
    }

    @PostMapping("/comments")
    public ResponseEntity<?> newComment(@RequestBody Comment newComment) throws URISyntaxException {
        Resource<Comment> resource = assembler.toResource(repository.save(newComment));

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }
}
