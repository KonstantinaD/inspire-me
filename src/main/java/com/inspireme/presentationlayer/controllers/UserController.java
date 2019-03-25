package com.inspireme.presentationlayer.controllers;

import com.inspireme.domainlayer.User;
import com.inspireme.domainlayer.UserType;
import com.inspireme.presentationlayer.assemblers.UserResourceAssembler;
import com.inspireme.presentationlayer.notfoundexceptions.UserNotFoundException;
import com.inspireme.servicelayer.services.UserService;
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
    private final UserResourceAssembler userAssembler;
    private final UserService userService;

    UserController(UserService userService,  UserResourceAssembler userAssembler) {
        this.userService = userService;
        this.userAssembler = userAssembler;
    }

    @GetMapping("/users")
    public Resources<Resource<User>> getAllUsers() {
        List<Resource<User>> users = userService.retrieveAllUsers().stream()
                .map(userAssembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(users,
                linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel()); //SelfRef comes from the Get mapping

    }

    @GetMapping("/users/{userId}")
    public Resource<User> getUserById(@PathVariable Long userId) {

        User user = userService.retrieveUser(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return userAssembler.toResource(user);
    }

    @PostMapping("/users")
    public ResponseEntity<?> createNewUser(@RequestBody User newUser) throws URISyntaxException {
        if (newUser.getUserType() == UserType.VISITOR) {

            Resource<User> userResource = userAssembler.toResource(userService.saveUser(newUser));

            return ResponseEntity
                    .created(new URI(userResource.getId().expand().getHref()))  //the italic is the http status
                    .body(userResource);
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new VndErrors.VndError("User Type not allowed", "You can't create a user whose user type is " + newUser.getUserType()));
      }

    @PutMapping("/users/{userId}")
    ResponseEntity<?> replaceUser(@RequestBody User newUser, @PathVariable Long userId) throws URISyntaxException {
        if (userId != 1) {
            if (newUser.getUserType() == UserType.VISITOR) {

                User updatedUser = userService.retrieveUser(userId)
                        .map(user -> {
                            user.setUserName(newUser.getUserName());
                            user.setUserType(newUser.getUserType());
                            return userService.saveUser(user);
                        })
                        .orElseGet(() -> {
                            return userService.saveUser(newUser);
                        });

                Resource<User> userResource = userAssembler.toResource(updatedUser);

                return ResponseEntity
                        .created(new URI(userResource.getId().expand().getHref()))
                        .body(userResource);
            }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new VndErrors.VndError("User Type not allowed", "You can't create a user whose user type is " + newUser.getUserType() + " and you can't set the user type of an existing user to " + newUser.getUserType()));
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new VndErrors.VndError("Updating the Admin user not allowed", "You can't update the user with user id " + userId + ". This is the Admin user."));

    }


    @DeleteMapping("users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        if (userId != 1) {
            userService.deleteUser(userId);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new VndErrors.VndError("Deleting the Admin user not allowed", "You can't delete the user with user id " + userId + ". This is the Admin user."));
    }
}

