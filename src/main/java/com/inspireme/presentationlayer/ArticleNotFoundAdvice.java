package com.inspireme.presentationlayer;

/*When an ArticleNotFoundException is thrown, this part of Spring MVC configuration is used to render an HTTP 404*/

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ArticleNotFoundAdvice {
    @ResponseBody  //signals that this advice is rendered straight into the response body
    @ExceptionHandler(ArticleNotFoundException.class) //configures the advice to only respond if an EmployeeNotFoundException is thrown
    @ResponseStatus(HttpStatus.NOT_FOUND)  //says to issues an HttpStatus.NOT_FOUND, i.e. an HTTP 404
    //The body of the advice generates the content. In this case, it gives the message of the exception.

    String employeeNotFoundHandler(ArticleNotFoundException ex) {
        return ex.getMessage();
    }
}
