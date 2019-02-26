package com.inspireme.domainlayer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Data
//@NoArgsConstructor
@EqualsAndHashCode(exclude = "articles")
@ToString(exclude = "articles")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<Article> articles;

    public Category (String categoryName, Article... articles) {
        this.categoryName = categoryName;
        this.articles = Stream.of(articles).collect(Collectors.toSet());
        this.articles.forEach(a -> a.setCategory(this));
    }
}
