package com.inspireme.controller;

import com.inspireme.controller.assemblers.UserResourceAssembler;
import com.inspireme.exception.UserNotFoundException;
import com.inspireme.model.User;
import com.inspireme.model.UserType;
import com.inspireme.service.SecurityService;
import com.inspireme.service.UserService;
import com.inspireme.validator.UserValidator;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Controller
//@RestController
//@RequestMapping(path = "/users")
public class UserController {
    private final UserResourceAssembler userAssembler;
    private final UserService userService;
    private final SecurityService securityService;
    private final UserValidator userValidator;

   public UserController(UserService userService, UserResourceAssembler userAssembler, SecurityService securityService, UserValidator userValidator) {
        this.userService = userService;
        this.userAssembler = userAssembler;
        this.securityService = securityService;
        this.userValidator = userValidator;
    }

    @GetMapping
    @ResponseBody
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
    @ResponseBody
    public Resource<User> getUser(@PathVariable Long userId) {

        User user = userService.retrieveUser(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return userAssembler.toResource(user);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> createNewUser(@RequestBody User newUser, @RequestBody Long roleId) throws URISyntaxException {
        if (newUser.getUserType() == UserType.VISITOR) {

            Resource<User> userResource = userAssembler.toResource(userService.saveUser(newUser, roleId)); //diff with tutorial

            return ResponseEntity
                    .created(new URI(userResource.getId().expand().getHref()))  //the italic is the http status
                    .body(userResource);
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new VndErrors.VndError("User Type Not Allowed", "You can't create a user whose user type is " + newUser.getUserType()));
    }

    @PutMapping("/{userId}")
    @ResponseBody
    ResponseEntity<?> replaceUser(@RequestBody User newUser, @RequestBody Long roleId, @PathVariable Long userId) throws URISyntaxException {
        if (userId != 1) {
            if (newUser.getUserType() == UserType.VISITOR) {

                User updatedUser = userService.retrieveUser(userId)
                        .map(user -> {
                            user.setUserName(newUser.getUserName());
                            user.setUserType(newUser.getUserType());
                            user.setRole(newUser.getRole());
                            return userService.saveUser(user, roleId); //?
                        })
                        .orElseGet(() -> {
                            return userService.saveUser(newUser, roleId);  //?
                        });

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
    @ResponseBody
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        if (userId != 1) {
            if (userService.retrieveUser(userId).isPresent()) {
                userService.deleteUser(userId);

                return ResponseEntity.noContent().build();
            }
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new VndErrors.VndError("User Not Found", "You can't delete the user with user id " + userId + ". This user doesn't exist."));
        }
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new VndErrors.VndError("Deleting the Admin User Not Allowed", "You can't delete the user with user id " + userId + ". This is the Admin user."));
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

//    @RequestMapping(value="/registration", method = RequestMethod.GET)
//    public ModelAndView registration(ModelAndView modelAndView){
////        ModelAndView modelAndView = new ModelAndView();
//        User user = new User();
//        modelAndView.addObject("user", user);
//        modelAndView.setViewName("registration");
//        return modelAndView;
//    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") User userForm, @ModelAttribute("roleId") Long roleId, BindingResult bindingResult) { //diff w tutorial
        userValidator.validate(userForm, bindingResult);
        userValidator.validate(roleId, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }
        userService.saveUser(userForm, roleId);

        securityService.autoLogin(userForm.getUserName(), userForm.getPasswordConfirm());

        return "redirect:/welcome";
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and/or password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }
//
////        public ModelAndView login(){
////            ModelAndView modelAndView = new ModelAndView();
////            modelAndView.setViewName("login");
////            return modelAndView;
//    }

//    @GetMapping("/login")
//    public String login(Map<String, Object> model) {
//        model.put("message", "HowToDoInJava Reader !!");
//        return "login";
//    }

        @GetMapping({"/", "/welcome"})
        public String welcome (Model model){
            return "welcome";
        }
    }




