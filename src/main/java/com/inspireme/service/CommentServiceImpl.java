package com.inspireme.service;

import com.inspireme.model.Article;
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

    @Override
    public Optional<Comment> retrieveComment(Long commentId) {
        return commentRepository.findById(commentId);
    }

    @Override
    public List<Comment> retrieveAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> retrieveAllCommentsPerArticle(Article article) {
        return commentRepository.findByArticle(article);
    }

    @Override
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }
}
