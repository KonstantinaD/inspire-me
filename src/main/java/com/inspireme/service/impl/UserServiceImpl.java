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
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(userId, User.class));
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
    public User updateUser(User newUser, Long userId) {

        User userToUpdate = retrieveUser(userId);

        userToUpdate.setUserName(newUser.getUserName());
        userToUpdate.setEmailAddress(newUser.getEmailAddress());
        userToUpdate.setPassword(newUser.getPassword());
//what to do with passwordConfirm - not required on the model because it doesn't come encoded in the POST/PUT response

        return saveUser(userToUpdate);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.delete(retrieveUser(userId));
    }

    @Override
    public User retrieveUserByUserName(String userName) {
       return userRepository.findByUserName(userName);
    }
}
