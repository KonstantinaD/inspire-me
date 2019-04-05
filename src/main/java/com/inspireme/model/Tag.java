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
import java.util.Set;

@Entity
@Data
@Builder
@EqualsAndHashCode(exclude = "articles")
@ToString(of = {"tagId", "tagName"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tagId", scope = Category.class, resolver = EntityIdResolver.class)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    private String tagName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Article> articles;
}
