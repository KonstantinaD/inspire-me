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
    @GeneratedValue
    private Long userId;

    private UserType userType;
    //private Date dateUserCreated;
    private LocalDateTime dateUserCreated;

    public User(UserType userType/*, Date dateUserCreated*/) {
        this.userType = userType;
        this.dateUserCreated = LocalDateTime.now(); //dateUserCreated;
    }
}
