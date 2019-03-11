package com.inspireme.presentationlayer.controllers;

import com.inspireme.domainlayer.User;
import com.inspireme.domainlayer.UserType;
import com.inspireme.infrastructurelayer.UserRepository;
import com.inspireme.presentationlayer.notfoundexceptions.UserNotFoundException;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class UserController {
    private final UserRepository userRepository;

    UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    @GetMapping("/users/{userId}")
//    public User getUserById(@PathVariable Long userId) {
//        return userRepository.findById(userId)
//                .orElseThrow((() -> new UserNotFoundException(userId)));
//    }

    //turn User into its resource-based version - Resource<User> - to properly render hypermedia
    @GetMapping("/users/{userId}")
    public Resource<User> getUserById(@PathVariable Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow((() -> new UserNotFoundException(userId)));

        //return articleAssembler.toResource(article);
        return new Resource<>(user,
                linkTo(methodOn(UserController.class).getUserById(user.getUserId())).withSelfRel());
    }

    @PostMapping("/users")
    public User createNewUser(@RequestBody User newUser) {
        if (newUser.getUserType() == UserType.VISITOR) {
            newUser.setDateUserCreated(LocalDateTime.now());
            return userRepository.save(newUser);
        }

        return null;
      }

//    @Component
//    class EmployeeResourceAssembler implements ResourceAssembler<Employee, Resource<Employee>> {
//
//        @Override
//        public Resource<Employee> toResource(Employee employee) {
//
//            return new Resource<>(employee,
//                    linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
//                    linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
//
//    @PostMapping("/employees")
//    ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) throws URISyntaxException {
//
//        Resource<Employee> resource = assembler.toResource(repository.save(newEmployee));
//
//        return ResponseEntity
//                .created(new URI(resource.getId().expand().getHref()))
//                .body(resource);
//    }

   //NOTE TO JAIME - I tried the below but it keeps giving me  this error   "message": "Not enough variable values available to expand 'userId'" :(

//    @PostMapping("/users")
//    public ResponseEntity<?> createNewUser(@RequestBody User newUser) throws URISyntaxException {
//        if (newUser.getUserType() == UserType.VISITOR) {
//            Resource<User> userResource = new Resource<>(newUser,
//                    linkTo(methodOn(UserController.class).getUserById(newUser.getUserId())).withSelfRel());
//
//            userRepository.save(newUser);
//
//            return ResponseEntity
//                    .created(new URI(userResource.getId().expand().getHref()))
//                    .body(userResource);
//        }
//
//        return ResponseEntity
//                .status(HttpStatus.FORBIDDEN)
//                .body(new VndErrors.VndError("User Type not allowed", "You can't add a user whose user type is " + newUser.getUserType()));
//    }

    @PutMapping("/users/{userId}")
    User replaceUser(@RequestBody User newUser, @PathVariable Long userId) {
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
            }
            return null;
            ////after adding ResponseEntity as part of REST add the below
//        return ResponseEntity
//                .status(HttpStatus.FORBIDDEN)
//                .body(new VndErrors.VndError("User Type not allowed", "You can't add a user whose user type is " + newUser.getUserType());
        }
        return null;
        ////after adding ResponseEntity as part of REST add the below
//        return ResponseEntity
//                .status(HttpStatus.FORBIDDEN)
//                .body(new VndErrors.VndError("Updating the Admin user not allowed", "You can't update the user with userId " + newUser.getUserId() + ". This is the Admin user.");

    }
    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        if (userId != 1) {
             userRepository.deleteById(userId);
  //          return ResponseEntity
//                .noContent().build();
//                .body(new VndErrors.VndError("Cannot delete the user with userId " + newUser.getUserId() + ". This is the Admin user.");
        }
    }
}

