package com.inspireme.service;

import com.inspireme.model.Article;
import com.inspireme.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Comment retrieveComment(Long commentId);

    List<Comment> retrieveAllComments();

    List<Comment> retrieveAllCommentsPerArticle(Long articleId);

    Comment saveComment(Comment comment);

    Comment updateComment(Comment newComment, Long commentId);

    void deleteComment(Long commentId);
}
