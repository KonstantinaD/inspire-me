package com.inspireme.service;

import com.inspireme.model.User;

import java.util.List;
import java.util.Optional;


public interface UserService {

    Optional<User> retrieveUser(Long userId);

    List<User> retrieveAllUsers();

    User saveUser(User user, Long roleId);

    void deleteUser(Long userId);

    User retrieveUserByUserName(String userName);
}
