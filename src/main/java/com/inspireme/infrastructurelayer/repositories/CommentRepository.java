package com.inspireme.infrastructurelayer.repositories;

import com.inspireme.domainlayer.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    @Query("SELECT c FROM Comment c WHERE c.article.articleId = :articleId")
    List<Comment> findByArticleId(@Param("articleId") Long articleId);
}
