package com.inspireme.exception;

public class CommentNotFoundException extends RuntimeException {

    private Long commentId;

    public CommentNotFoundException(Long commentId) {
        this.commentId = commentId;
    }

    public Long getCommentId() {
        return commentId;
    }
}
