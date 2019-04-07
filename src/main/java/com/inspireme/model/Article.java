package com.inspireme.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.inspireme.controller.converters.EntityIdResolver;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Builder
@EqualsAndHashCode(exclude = "comments")
//@ToString(of = {"articleId", "articleTitle"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "articleId", scope = Article.class, resolver = EntityIdResolver.class)
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    private String articleTitle;

    @ToString.Exclude
    private String articleText;

    @ToString.Exclude
    private String imageUrl;

    @ToString.Exclude
    private LocalDateTime dateArticlePublished = LocalDateTime.now();

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    @ToString.Exclude
    private Set<Comment> comments;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    @ToString.Exclude
    private Category category;

    @ManyToOne
    @JoinColumn
    @ToString.Exclude
    private User articlePublishedBy;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "article_tags",
            joinColumns = { @JoinColumn(name = "article_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") })
    private Set<Tag> tags;

//    @PrePersist
//    public void addTag(Tag tag) {
//
//        this.tags.add(tag);
//
//        if (!tag.getArticles().contains(this)) {
//            tag.add(this);
//        }
//    }
//
//    @PrePersist
//    public void populateTags {
//      for (Tag tag : tags) {
//          if (!tag.getArticles().contains(this)) {
//            tag.addToArticleList(this);
//        }
//      }
//    }

}
