package com.inspireme.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class Role {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long roleId;

        private String roleName;

        @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
        @JsonBackReference
        @ToString.Exclude
        private Set<User> users;
    }
