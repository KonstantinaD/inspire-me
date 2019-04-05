package com.inspireme.service;

import com.inspireme.model.User;
import com.inspireme.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

   private final UserRepository userRepository;

   public UserServiceImpl(UserRepository userRepository) {
       this.userRepository = userRepository;
   }

    @Override
    public Optional<User> retrieveUser(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<User> retrieveAllUsers() {
       return userRepository.findAll();
    }

    @Override
    public User saveUser(User user) {
       return userRepository.save(user);
    }

    @Override
    public void deleteUser (User user) {
       userRepository.delete(user);
    }

}
