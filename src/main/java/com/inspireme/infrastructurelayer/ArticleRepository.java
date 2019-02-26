package com.inspireme.infrastructurelayer;

import com.inspireme.domainlayer.Article;
import com.inspireme.domainlayer.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    }

