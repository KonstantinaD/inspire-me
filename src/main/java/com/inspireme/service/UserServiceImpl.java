package com.inspireme.service;

import com.inspireme.model.User;
import com.inspireme.model.UserType;
import com.inspireme.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

   private final UserRepository userRepository;

   private final BCryptPasswordEncoder bCryptPasswordEncoder;

   public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
       this.userRepository = userRepository;
       this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setUserType(UserType.VISITOR);
       return userRepository.save(user);
    }

    @Override
    public void deleteUser (Long userId) {
       userRepository.deleteById(userId);
    }

    @Override
    public User retrieveUserByUserName(String userName) {
       return userRepository.findByUserName(userName);
    }


}
