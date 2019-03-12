package com.inspireme.domainlayer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
//@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;

    private UserType userType;

    private LocalDateTime dateUserCreated;

    @JsonCreator
    public User(@JsonProperty("userName") String userName, @JsonProperty("userType") UserType userType) {
        this.userName = userName;
        this.userType = userType;
        this.dateUserCreated = LocalDateTime.now();
    }
}
