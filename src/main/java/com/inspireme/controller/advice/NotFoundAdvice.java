package com.inspireme.controller.advice;

/*When e.g. an ArticleNotFoundException is thrown, this part of Spring MVC configuration is used to render an HTTP 404*/

import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.UnresolvedId;
import com.inspireme.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class NotFoundAdvice {

    @ExceptionHandler(ArticleNotFoundException.class)  //configures the advice to only respond if an ArticleNFE is thrown
    public ResponseEntity<?> handleArticleNotFoundException(ArticleNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(getBody(("Couldn't find article with article id " + e.getArticleId())));  //the body of the advice generates the content
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<?> handleCommentNotFoundException(CommentNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(getBody(( "Couldn't find comment with comment id " + e.getCommentId())));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(getBody(("Couldn't find user with user id " + e.getUserId())));
    }

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<?> handleTagNotFoundException(TagNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(getBody(("Couldn't find tag with tag id " + e.getTagId())));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<?> handle(CategoryNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(getBody("Couldn't find category with category id " + e.getCategoryId()));
    }

    @ExceptionHandler(UnresolvedForwardReference.class)
    public ResponseEntity<?> handleMessageNotReadableException(UnresolvedForwardReference exception) {

        List<String> errorList = exception.getUnresolvedIds().stream()
                .map(this::buildUnresolvedIdMessage)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(getBody(errorList));
    }

    private String buildUnresolvedIdMessage(UnresolvedId unresolvedId){
        return "Couldn't find " + unresolvedId.getType().getSimpleName() + " with id " + unresolvedId.getId();
    }

    private Map<String, Object> getBody(String error){
        return getBody(Collections.singletonList(error));
    }
    
    private Map<String, Object> getBody(List<String> errors){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("errors", errors);

        return body;
    }

}
