package com.inspireme.service;

import com.inspireme.model.Comment;
import com.inspireme.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Optional<Comment> retrieveComment(Long commentId) {
        return commentRepository.findById(commentId);
    }

    public List<Comment> retrieveAllComments() {
        return commentRepository.findAll();
    }

    public List<Comment> retrieveAllCommentsPerArticle(Long articleId) {
        return commentRepository.findByArticleId(articleId);
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
