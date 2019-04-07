package com.inspireme.controller.advice;

/*When e.g. an ArticleNotFoundException is thrown, this part of Spring MVC configuration is used to render an HTTP 404*/

import com.inspireme.exception.ArticleNotFoundException;
import com.inspireme.exception.CommentNotFoundException;
import com.inspireme.exception.TagNotFoundException;
import com.inspireme.exception.UserNotFoundException;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NotFoundAdvice {

    @ExceptionHandler(ArticleNotFoundException.class)  //configures the advice to only respond if an ArticleNFE is thrown
    public ResponseEntity<?> handleArticleNotFoundException(ArticleNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new VndErrors.VndError("Article Not Found", "Couldn't find article with article id " + e.getArticleId()));  //the body of the advice generates the content
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<?> handleCommentNotFoundException(CommentNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new VndErrors.VndError("Comment Not Found", "Couldn't find comment with comment id " + e.getCommentId()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new VndErrors.VndError("User Not Found", "Couldn't find user with user id " + e.getUserId()));
    }

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<?> handleTagNotFoundException(TagNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new VndErrors.VndError("Tag Not Found", "Couldn't find tag with tag id " + e.getTagId()));
    }
}
