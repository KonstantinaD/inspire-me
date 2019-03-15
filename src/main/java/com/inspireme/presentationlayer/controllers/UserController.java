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
    private final UserRepository repository;
    private final UserResourceAssembler assembler;

    UserController(UserRepository repository,  UserResourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/users")
    public Resources<Resource<User>> getAllUsers() {
        List<Resource<User>> users = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(users,
                linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel()); //SelfRef comes from the Get mapping

    }

    @GetMapping("/users/{userId}")
    public Resource<User> getUserById(@PathVariable Long userId) {

        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return assembler.toResource(user);
    }

    @PostMapping("/users")
    public ResponseEntity<?> createNewUser(@RequestBody User newUser) throws URISyntaxException {
        if (newUser.getUserType() == UserType.VISITOR) {

            Resource<User> resource = assembler.toResource(repository.save(newUser));

            return ResponseEntity
                    .created(new URI(resource.getId().expand().getHref()))  //the italic is the http status
                    .body(resource);
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new VndErrors.VndError("User Type not allowed", "You can't add a user whose user type is " + newUser.getUserType()));
      }

    @PutMapping("/users/{userId}")
    ResponseEntity<?> replaceUser(@RequestBody User newUser, @PathVariable Long userId) throws URISyntaxException{
        if (userId != 1) {
            if (newUser.getUserType() == UserType.VISITOR) {

                User updatedUser = repository.findById(userId)
                        .map(user -> {
                            user.setUserName(newUser.getUserName());
                            user.setUserType(newUser.getUserType());
                            return repository.save(user);
                        })
                        .orElseGet(() -> {
                            //newUser.setUserId(userId);
                            return repository.save(newUser);
                        });

                Resource<User> resource = assembler.toResource(updatedUser);

                return ResponseEntity
                        .created(new URI(resource.getId().expand().getHref()))
                        .body(resource);
            }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new VndErrors.VndError("User Type not allowed", "You can't add a user whose user type is " + newUser.getUserType()));
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new VndErrors.VndError("Updating the Admin user not allowed", "You can't update the user with userId " + userId + ". This is the Admin user."));

    }


    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        if (userId != 1) {
            repository.deleteById(userId);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new VndErrors.VndError("Deleting the Admin user not allowed", "You can't delete the user with userId " + userId + ". This is the Admin user."));
    }
}

