package com.inspireme.presentationlayer.notfoundexceptions;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(Long commentId) {
        super("Could not find comment " + commentId);
    }
}
