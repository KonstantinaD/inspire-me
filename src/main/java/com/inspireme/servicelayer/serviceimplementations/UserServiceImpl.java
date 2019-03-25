package com.inspireme.servicelayer.serviceimplementations;

import com.inspireme.domainlayer.User;
import com.inspireme.infrastructurelayer.repositories.UserRepository;
import com.inspireme.servicelayer.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

   private final UserRepository userRepository;

   public UserServiceImpl(UserRepository userRepository) {
       this.userRepository = userRepository;
   }

    public Optional<User> retrieveUser(Long userId) {
        return userRepository.findById(userId);
    }

    public List<User> retrieveAllUsers() {
       return userRepository.findAll();
    }

    public User saveUser(User user) {
       return userRepository.save(user);
    }

    public void deleteUser (Long userId) {
       userRepository.deleteById(userId);
    }

}
