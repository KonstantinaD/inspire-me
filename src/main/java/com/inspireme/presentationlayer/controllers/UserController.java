package com.inspireme.presentationlayer.controllers;

import com.inspireme.domainlayer.User;
import com.inspireme.domainlayer.UserType;
import com.inspireme.infrastructurelayer.UserRepository;
import com.inspireme.presentationlayer.assemblers.UserResourceAssembler;
import com.inspireme.presentationlayer.notfoundexceptions.UserNotFoundException;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class UserController {
    private final UserRepository userRepository;
    private final UserResourceAssembler userAssembler;

    UserController(UserRepository userRepository,  UserResourceAssembler userAssembler) {
        this.userRepository = userRepository;
        this.userAssembler = userAssembler;
    }

    @GetMapping("/users")
    public Resources<Resource<User>> getAllUsers() {
        List<Resource<User>> users = userRepository.findAll().stream()
                .map(userAssembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(users,
                linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel()); //SelfRef comes from the Get mapping

    }

    @GetMapping("/users/{userId}")
    public Resource<User> getUserById(@PathVariable Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return userAssembler.toResource(user);
    }

    @PostMapping("/users")
    public ResponseEntity<?> createNewUser(@RequestBody User newUser) throws URISyntaxException {
        if (newUser.getUserType() == UserType.VISITOR) {

            Resource<User> userResource = userAssembler.toResource(userRepository.save(newUser));

            return ResponseEntity
                    .created(new URI(userResource.getId().expand().getHref()))  //the italic is the http status
                    .body(userResource);
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new VndErrors.VndError("User Type not allowed", "You can't add a user whose user type is " + newUser.getUserType()));
      }

    @PutMapping("/users/{userId}")
    ResponseEntity<?> replaceUser(@RequestBody User newUser, @PathVariable Long userId) throws URISyntaxException {
        if (userId != 1) {
            if (newUser.getUserType() == UserType.VISITOR) {

                User updatedUser = userRepository.findById(userId)
                        .map(user -> {
                            user.setUserName(newUser.getUserName());
                            user.setUserType(newUser.getUserType());
                            return userRepository.save(user);
                        })
                        .orElseGet(() -> {
                            //newUser.setUserId(userId);
                            return userRepository.save(newUser);
                        });

                Resource<User> userResource = userAssembler.toResource(updatedUser);

                return ResponseEntity
                        .created(new URI(userResource.getId().expand().getHref()))
                        .body(userResource);
            }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new VndErrors.VndError("User Type not allowed", "You can't add a user whose user type is " + newUser.getUserType()));
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new VndErrors.VndError("Updating the Admin user not allowed", "You can't update the user with userId " + userId + ". This is the Admin user."));

    }


    @DeleteMapping("users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        if (userId != 1) {
            userRepository.deleteById(userId);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new VndErrors.VndError("Deleting the Admin user not allowed", "You can't delete the user with userId " + userId + ". This is the Admin user."));
    }
}

