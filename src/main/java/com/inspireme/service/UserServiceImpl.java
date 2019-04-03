package com.inspireme.service;

import com.inspireme.model.User;
import com.inspireme.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

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
    public void deleteUser (Long userId) {
       userRepository.deleteById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

       return Optional.ofNullable(userRepository.findByUserName(userName))
               .map(user -> new org.springframework.security.core.userdetails.User(
                       user.getUserName(), user.getPassword(), emptyList())
               )
               .orElseThrow(() -> new UsernameNotFoundException(userName));
    }
}
