package com.inspireme.repository;
import com.inspireme.model.Article;
import com.inspireme.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long>{

    @Query("SELECT t FROM Tag t WHERE :article MEMBER OF t.articles")
    List<Tag> findByArticle(@Param("article") Article article);

//    @Query("DELETE FROM article_tags a_t WHERE article MEMBER OF a_t AND article = :article AND :tag MEMBER OF article.tags")
//    void deleteByArticle(@Param("article") Article article, @Param("tag") Tag tag);
}
