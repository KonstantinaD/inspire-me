package com.inspireme.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.inspireme.controller.converters.EntityIdResolver;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(exclude = "articles")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId", scope = User.class, resolver = EntityIdResolver.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;

    @ToString.Exclude
    private String password;

    private UserType userType;

    private LocalDateTime dateUserCreated = LocalDateTime.now();

    @OneToMany(mappedBy = "articlePublishedBy", cascade = CascadeType.PERSIST)
    @JsonBackReference
    @ToString.Exclude
    private Set<Article> articles;
}
