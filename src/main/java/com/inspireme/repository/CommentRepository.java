package com.inspireme.repository;

import com.inspireme.model.Article;
import com.inspireme.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.article = :article")
    List<Comment> findByArticle(@Param("article") Article article);
}
