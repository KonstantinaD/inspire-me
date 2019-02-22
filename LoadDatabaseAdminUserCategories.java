package com.inspireme.infrastructurelayer;

import com.inspireme.domainlayer.Article;
import com.inspireme.domainlayer.Category;
import com.inspireme.domainlayer.User;
import com.inspireme.domainlayer.UserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabaseAdminUserCategories {
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, CategoryRepository categoryRepository) {
        return args -> {
            log.info("Preloading " + userRepository.save(new User(UserType.ADMIN)));

            userRepository.findAll().forEach(user -> {
                log.info("Preloaded " + user);
            });

            log.info("Preloading " + categoryRepository.save(new Category("Personal Development")));
            log.info("Preloading " + categoryRepository.save(new Category("Mental Health")));
            log.info("Preloading " + categoryRepository.save(new Category("Psychology")));
            log.info("Preloading " + categoryRepository.save(new Category("Spirituality")));

            categoryRepository.findAll().forEach(category -> {
                log.info("Preloaded " + category);
            });
        };
    }
}
