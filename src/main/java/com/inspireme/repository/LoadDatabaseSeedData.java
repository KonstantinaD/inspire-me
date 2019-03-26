package com.inspireme.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabaseSeedData {
//    @Bean
//    CommandLineRunner initDatabase(CommentRepository commentRepository, ArticleRepository articleRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
//        User admin = new User(UserType.ADMIN);
//        User visitor1 = new User(UserType.VISITOR);
//        User visitor2 = new User(UserType.VISITOR);
//        userRepository.save(admin);
//        userRepository.save(visitor1);
//        userRepository.save(visitor2);
//
//        userRepository.findAll().forEach(user -> {
//            log.info("Preloaded " + user);
//        });
//
//        Category category1 = new Category("Mental Health");
//        Category category2 = new Category("Psychology");
//        Category category3 = new Category("Personal Development");
//        Category category4 = new Category("Spirituality");
//        categoryRepository.save(category1);
//        categoryRepository.save(category2);
//        categoryRepository.save(category3);
//        categoryRepository.save(category4);
//
//        categoryRepository.findAll().forEach(category -> {
//            log.info("Preloaded " + category);
//        });
//
//        Article article1 = new Article("Article title 1", "Some sample article", "https://bit.ly/2tC5qX3", category1, admin);
//        Article article2 = new Article("Article title 2", "Another sample article", "https://bit.ly/2BHiSO3", category2, admin);
//
//        articleRepository.save(article1);
//        articleRepository.save(article2);
//
//        articleRepository.findAll().forEach(article -> {
//            log.info("Preloaded " + article);
//        });
//
//        Comment comment1 = new Comment("Some sample comment", article1, visitor1);
//        Comment comment2 = new Comment("Another sample comment", article2, visitor2);
//        commentRepository.save(comment1);
//        commentRepository.save(comment2);
//
//        commentRepository.findAll().forEach(comment -> {
//            log.info("Preloaded " + comment);
//        });
//
//        return args -> {
//        };
//    }
}