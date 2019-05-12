package com.inspireme.service;

import com.inspireme.model.User;

import java.util.List;


public interface UserService {

    User retrieveUser(Long userId);

    List<User> retrieveAllUsers();

    User saveUser(User user);

    void deleteUser(Long userId);

    User retrieveUserByUserName(String userName);

    User replaceUser(Long articleId, User newUser);
}
