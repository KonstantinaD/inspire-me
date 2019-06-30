package com.inspireme.controller.advice;

import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.UnresolvedId;
import com.inspireme.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class NotFoundAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(NotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(getBody(("Couldn't find "+ e.getEntity() + " with id " + e.getId())));
    }

    @ExceptionHandler(UnresolvedForwardReference.class)
    public ResponseEntity<Map<String, Object>> handleMessageNotReadableException(UnresolvedForwardReference exception) {

        List<String> errorList = exception.getUnresolvedIds().stream()
                .map(this::buildUnresolvedIdMessage)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(getBodyForList(errorList));
    }

    private String buildUnresolvedIdMessage(UnresolvedId unresolvedId){
        return "Couldn't find [" + unresolvedId.getType().getSimpleName() + "] with id " + unresolvedId.getId();
    }

    private Map<String, Object> getBody(String error){
        return getBodyForList(Collections.singletonList(error));
    }
    
    private Map<String, Object> getBodyForList(List<String> errors){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("errors", errors);
        return body;
    }
}
