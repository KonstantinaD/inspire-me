package com.inspireme.presentationlayer.controllers;

import com.inspireme.domainlayer.Article;
import com.inspireme.infrastructurelayer.ArticleRepository;
import com.inspireme.infrastructurelayer.CommentRepository;
import com.inspireme.infrastructurelayer.UserRepository;
import com.inspireme.presentationlayer.notfoundexceptions.ArticleNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class CommentController {
    private final CommentRepository commentRepository;
    //private final UserRepository userRepository;

    CommentController (CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }


}
