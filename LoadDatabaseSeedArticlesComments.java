package com.inspireme.infrastructurelayer;

import com.inspireme.domainlayer.Article;
import com.inspireme.domainlayer.Comment;
import com.inspireme.domainlayer.UserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabaseSeedArticlesComments {
    @Bean
    CommandLineRunner initDatabase(ArticleRepository articleRepository, CommentRepository commentRepository) {
        return args -> {
            log.info("Preloading " + articleRepository.save(new Article("Article title 1", "Some sample article", "https://bit.ly/2tC5qX3", "Mental Health", UserType.ADMIN)));
            log.info("Preloading " + articleRepository.save(new Article("Another sample article", "https://bit.ly/2BHiSO3")));

            articleRepository.findAll().forEach(article -> {
                log.info("Preloaded " + article);
            });

            log.info("Preloading " + commentRepository.save(new Comment("Some sample comment")));
            log.info("Preloading " + commentRepository.save(new Comment("Another sample comment")));

            commentRepository.findAll().forEach(comment -> {
                log.info("Preloaded " + comment);
            });
        };
    }
}
