package com.inspireme.repository;

import com.inspireme.model.Article;
import com.inspireme.model.Category;
import com.inspireme.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("SELECT a FROM Article a WHERE a.category = :category")
    List<Article> findByCategory(@Param("category") Category category);

    @Query("SELECT a FROM Article a WHERE :tag MEMBER OF a.tags")
    List<Article> findByTag(@Param("tag") Tag tag);

    List<Article> findAllByArticlePublishedByUserId(Long userId);
}

