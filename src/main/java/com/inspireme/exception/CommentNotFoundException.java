package com.inspireme.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(Long commentId) {
        super("Could not find comment " + commentId);
    }
}
