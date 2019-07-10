package com.inspireme.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.inspireme.controller.converters.EntityIdResolver;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(exclude = "articles")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId", scope = User.class,
        resolver = EntityIdResolver.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotEmpty(message = "Please provide a username")
    private String userName;

    @ToString.Exclude
    @NotEmpty(message = "Please provide an email address")
    @Email
    private String emailAddress;

    private UserType userType;

    @ToString.Exclude
    private LocalDateTime dateUserCreated = LocalDateTime.now();

    @ToString.Exclude
    @NotEmpty(message = "Please provide a password")
    private String password;

    /**
     * The usage of the 2 password fields will be finalised in the JSON responses when the user authentication through
     * the React App is finalised
     */
    @Transient
    @ToString.Exclude
    private String passwordConfirm;

    @OneToMany(mappedBy = "articlePublishedBy", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonBackReference
    @ToString.Exclude
    private Set<Article> articles;
}
