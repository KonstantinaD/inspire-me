package com.inspireme.infrastructurelayer;

import com.inspireme.domainlayer.Article;
import com.inspireme.domainlayer.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
   List<Article> findByCategory(@Param("categoryId") Category category);
//    void saveByCategoryId(Article article, Long categoryId);
}

