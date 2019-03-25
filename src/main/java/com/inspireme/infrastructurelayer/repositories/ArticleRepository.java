package com.inspireme.infrastructurelayer.repositories;

import com.inspireme.domainlayer.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT a FROM Article a WHERE a.category.categoryId = :categoryId")
    List<Article> findByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT a FROM Article a WHERE a.category.categoryId != :categoryId")
    List<Article> findFromOtherCategoryIds(@Param("categoryId") Long categoryId);
}

