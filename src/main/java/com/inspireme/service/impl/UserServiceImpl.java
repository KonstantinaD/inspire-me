package com.inspireme.service.impl;

import com.inspireme.exception.NotFoundException;
import com.inspireme.model.User;
import com.inspireme.model.UserType;
import com.inspireme.repository.UserRepository;
import com.inspireme.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

   private final UserRepository userRepository;

   private final BCryptPasswordEncoder bCryptPasswordEncoder;

   public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
       this.userRepository = userRepository;
       this.bCryptPasswordEncoder = bCryptPasswordEncoder;
   }

    @Override
    public User retrieveUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId, User.class));
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
    public void deleteUser(Long userId) {
        userRepository.delete(retrieveUser(userId));
    }

    @Override
    public User retrieveUserByUserName(String userName) {
       return userRepository.findByUserName(userName);
    }

    @Override
    public User replaceUser(Long articleId, User newUser) {

        User updateUser = userRepository.findById(articleId)
                .orElseGet(User::new);

        updateUser.setUserName(newUser.getUserName());

        return saveUser(updateUser);
    }

}
