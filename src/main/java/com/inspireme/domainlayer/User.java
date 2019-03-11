package com.inspireme.domainlayer;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
//@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;

    private UserType userType;
    //private Date dateUserCreated;
    private LocalDateTime dateUserCreated;

    public User(String userName, UserType userType/*, Date dateUserCreated*/) {
        this.userName = userName;
        this.userType = userType;
        this.dateUserCreated = LocalDateTime.now(); //dateUserCreated;
    }
}
