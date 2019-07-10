package com.inspireme.service.impl;

import com.inspireme.exception.NotFoundException;
import com.inspireme.model.Article;
import com.inspireme.model.Comment;
import com.inspireme.repository.ArticleRepository;
import com.inspireme.repository.CommentRepository;
import com.inspireme.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    public CommentServiceImpl(CommentRepository commentRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public Comment retrieveComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(commentId, Comment.class));
    }

    @Override
    public List<Comment> retrieveAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> retrieveAllCommentsPerArticle(Long articleId) {

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundException(articleId, Article.class));

        return commentRepository.findByArticle(article);
    }

    @Override
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    /**
     * After the user authentication is finalised, on the front end the comment will be edited only by the logged-in
     * user who posted it, and on the article the comment was initially posted on, hence updating the comment
     * publisher and the respective article is not available through Postman
     */
    @Override
    public Comment updateComment(Comment newComment, Long commentId) {

        Comment commentToUpdate = retrieveComment(commentId);

        commentToUpdate.setCommentText(newComment.getCommentText());

        return saveComment(commentToUpdate);
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.delete(retrieveComment(commentId));
    }
}
