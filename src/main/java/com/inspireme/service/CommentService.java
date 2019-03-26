package com.inspireme.service;

import com.inspireme.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<Comment> retrieveComment(Long commentId);

    List<Comment> retrieveAllComments();

    List<Comment> retrieveAllCommentsPerArticle(Long articleId);

    Comment saveComment(Comment comment);

    void deleteComment(Long commentId);
}
