package com.inspireme.repository;

import com.inspireme.model.Article;
import com.inspireme.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT a FROM Article a WHERE a.category = :category")
    List<Article> findByCategory(@Param("category") Category category);

    @Query("SELECT a FROM Article a WHERE a.category != :category")
    List<Article> findFromOtherCategories(@Param("category") Category category);
}

