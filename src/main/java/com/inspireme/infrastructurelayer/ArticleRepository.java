package com.inspireme.infrastructurelayer;

import com.inspireme.domainlayer.Article;
import com.inspireme.domainlayer.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
//    List<Article> findAllByCategoryId(Long categoryId);
//    void saveByCategoryId(Article article, Long categoryId);
}

