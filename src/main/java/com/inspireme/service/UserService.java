package com.inspireme.service;

import com.inspireme.model.User;

import java.util.List;


public interface UserService {

    User retrieveUser(Long userId);

    List<User> retrieveAllUsers();

    User saveUser(User user);

    User updateUser(User newUser, Long userId);

    void deleteUser(Long userId);

    User retrieveUserByUserName(String userName);
}
