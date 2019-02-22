package com.inspireme.infrastructurelayer;

import com.inspireme.domainlayer.Article;
import org.springframework.data.jpa.repository.JpaRepository;

interface ArticleRepository extends JpaRepository<Article, Long> {
}
