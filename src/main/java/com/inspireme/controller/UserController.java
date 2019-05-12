package com.inspireme.controller;

import com.inspireme.controller.assemblers.UserResourceAssembler;
import com.inspireme.model.User;
import com.inspireme.model.UserType;
import com.inspireme.service.UserService;
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
@RequestMapping(path = "/users")
public class UserController {
    private final UserResourceAssembler userAssembler;
    private final UserService userService;

   public UserController(UserService userService, UserResourceAssembler userAssembler) {
        this.userService = userService;
        this.userAssembler = userAssembler;
    }

    @GetMapping
    public Resources<Resource<User>> getAllUsers() {
        if (!userService.retrieveAllUsers().isEmpty()) {
            List<Resource<User>> users = userService.retrieveAllUsers().stream()
                    .map(userAssembler::toResource)
                    .collect(Collectors.toList());

            return new Resources<>(users,
                    linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel()); //SelfRef comes from the Get mapping
        }
        return null;
    }

    @GetMapping("/{userId}")
    public Resource<User> getUser(@PathVariable Long userId) {

        User user = userService.retrieveUser(userId);
        return userAssembler.toResource(user);
    }

    @PostMapping
    public ResponseEntity<?> createNewUser(@RequestBody User newUser) throws URISyntaxException {
        if (newUser.getUserType() == UserType.VISITOR) {

            Resource<User> userResource = userAssembler.toResource(userService.saveUser(newUser)); //diff with tutorial

            return ResponseEntity
                    .created(new URI(userResource.getId().expand().getHref()))  //the italic is the http status
                    .body(userResource);
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new VndErrors.VndError("User Type Not Allowed", "You can't create a user whose user type is " + newUser.getUserType()));
    }

    @PutMapping("/{userId}")
    ResponseEntity<?> replaceUser(@RequestBody User newUser,  @PathVariable Long userId) throws URISyntaxException {
        if (userId != 1) {
            if (newUser.getUserType() == UserType.VISITOR) {

                User updatedUser = userService.replaceUser(userId, newUser);
                Resource<User> userResource = userAssembler.toResource(updatedUser);

                return ResponseEntity
                        .created(new URI(userResource.getId().expand().getHref()))
                        .body(userResource);
            }

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new VndErrors.VndError("User Type Not Allowed", "You can't create a user whose user type is " + newUser.getUserType() + " and you can't set the user type of an existing user to " + newUser.getUserType()));
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new VndErrors.VndError("Updating the Admin User Not Allowed", "You can't update the user with user id " + userId + ". This is the Admin user."));

    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        if (userId != 1) {
            userService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new VndErrors.VndError("Deleting the Admin User Not Allowed", "You can't delete the user with user id " + userId + ". This is the Admin user."));
    }
}




