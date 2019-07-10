package com.inspireme.controller;

import com.inspireme.controller.assemblers.UserResourceAssembler;
import com.inspireme.model.User;
import com.inspireme.service.UserService;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.VndErrors;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
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
    public Resources<?> getAllUsers() {

       List<User> users = userService.retrieveAllUsers();

        if (!users.isEmpty()) {
            List<Resource<User>> userResources = users.stream()
                    .map(userAssembler::toResource)
                    .collect(Collectors.toList());

            return new Resources<>(userResources,
                    linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
        }
        return new Resources<>(Arrays.asList(getEmptyListUserWrapper()),
                linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
    }

    @GetMapping("/{userId}")
    public Resource<User> getUser(@PathVariable Long userId) {

        return userAssembler.toResource(userService.retrieveUser(userId));
    }

    @PostMapping
    public ResponseEntity<?> createNewUser(@RequestBody @Valid User newUser) throws URISyntaxException {
        /**
         * The below is disabled due to unfinished user authentication on the React app. Enable for Postman testing.
         * Once the authentication is finalised, the users will be created through a front end Registration
         * process
         */
//        if (newUser.getUserType() == UserType.VISITOR) {

            Resource<User> userResource = userAssembler.toResource(userService.saveUser(newUser));

            return ResponseEntity
                    .created(new URI(userResource.getId().expand().getHref()))
                    .body(userResource);
//        }
//
//        return ResponseEntity
//                .status(HttpStatus.FORBIDDEN)
//                .body(new VndErrors.VndError("User Type Not Allowed", "You can't create a user whose user type is "
// + newUser.getUserType()));
    }

    @PutMapping("/{userId}")
    ResponseEntity<?> editUser(@RequestBody @Valid User newUser,  @PathVariable Long userId) throws URISyntaxException {
        if (userId != 1) {
            /**
             * The below is disabled due to unfinished user authentication on the React app. Enable for Postman testing.
             * Once the authentication is finalised, the user details will be edited through a front end
             * Login process
             */
//            if (newUser.getUserType() == UserType.VISITOR) {

                User updatedUser = userService.updateUser(newUser, userId);
                Resource<User> userResource = userAssembler.toResource(updatedUser);

                return ResponseEntity
                        .created(new URI(userResource.getId().expand().getHref()))
                        .body(userResource);
        }
//
//            return ResponseEntity
//                    .status(HttpStatus.FORBIDDEN)
//                    .body(new VndErrors.VndError("User Type Not Allowed", "You can't create a user whose user type
// is " + newUser.getUserType() + " and you can't set the user type of an existing user to " + newUser.getUserType()));
//        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new VndErrors.VndError("Editing the Admin User Not Allowed", "You can't edit " +
                        "the user with user id " + userId + ". This is the Admin user."));

    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> removeUser(@PathVariable Long userId) {
        if (userId != 1) {
            userService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new VndErrors.VndError("Deleting the Admin User Not Allowed", "You can't " +
                        "delete the user with user id " + userId + ". This is the Admin user."));
    }

    private EmbeddedWrapper getEmptyListUserWrapper(){
        return new EmbeddedWrappers(false).emptyCollectionOf(User.class);
    }
}




