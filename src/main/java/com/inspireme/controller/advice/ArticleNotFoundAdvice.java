package com.inspireme.controller.advice;

/*When an ArticleNotFoundException is thrown, this part of Spring MVC configuration is used to render an HTTP 404*/

import com.inspireme.exception.ArticleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ArticleNotFoundAdvice {

    @ExceptionHandler(ArticleNotFoundException.class)
    public ResponseEntity<ErrorMsg> handleArticleNotFoundException(ArticleNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorMsg("article.not.found", "Article Not Found: " + e.getArticleId()));
    }
}
