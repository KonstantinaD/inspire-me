package com.inspireme.service;

//We create SecurityService to provide the current logged-in username, and auto login user after registration

public interface SecurityService {
    String findLoggedInUsername();

    void autoLogin(String username, String password);
}
