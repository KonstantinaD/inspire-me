package com.inspireme.presentationlayer.notfoundexceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("Could not find user " + userId);
    }
}
