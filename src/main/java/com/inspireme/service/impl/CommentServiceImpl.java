package com.inspireme.service.impl;

import com.inspireme.exception.NotFoundException;
import com.inspireme.model.Article;
import com.inspireme.model.Comment;
import com.inspireme.repository.ArticleRepository;
import com.inspireme.repository.CommentRepository;
import com.inspireme.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Comment updateComment(Comment newComment, Long commentId) {

        Comment commentToUpdate = retrieveComment(commentId);

        commentToUpdate.setCommentText(newComment.getCommentText());
        //ON the UI they won't be able to move the comment to another article - the below is deactivated
//        commentToUpdate.setArticle(newComment.getArticle());
        //ONLY THE SAME USER CAN UPDATE THEIR OWN COMMENT - below is deactivated - maybe better with PERMISSIONS
//        commentToUpdate.setCommentPublishedBy(newComment.getCommentPublishedBy());

        return saveComment(commentToUpdate);
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.delete(retrieveComment(commentId));
    }
}
