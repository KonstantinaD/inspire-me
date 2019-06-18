package com.inspireme.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.inspireme.controller.converters.EntityIdResolver;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "commentId", scope = Comment.class, resolver = EntityIdResolver.class)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ToString.Exclude
    @NotEmpty(message = "Please provide text for the comment")
    private String commentText;

    @ToString.Exclude
    private LocalDateTime dateCommentPublished = LocalDateTime.now();

    @ManyToOne/*(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)*/
    @JoinColumn(nullable = false)
    @ToString.Exclude
    @NotNull(message = "Please provide an article")
    private Article article;

    @ManyToOne
    @JoinColumn
    @ToString.Exclude
    @NotNull(message = "Please provide a comment publisher")
    private User commentPublishedBy;
}
