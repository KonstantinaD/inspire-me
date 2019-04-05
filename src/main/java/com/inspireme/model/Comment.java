package com.inspireme.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.inspireme.controller.converters.EntityIdResolver;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
//@ToString(of = {"commentId", "dateCommentPublished"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "commentId", scope = Comment.class, resolver = EntityIdResolver.class)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ToString.Exclude
    private String commentText;

    @ToString.Exclude
    private LocalDateTime dateCommentPublished = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    @ToString.Exclude
    private Article article;

    @ManyToOne
    @JoinColumn
    @ToString.Exclude
    private User commentPublishedBy;
}
