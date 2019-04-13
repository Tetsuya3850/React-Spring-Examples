package com.example.blogserver.user;

public class ApplicationUserNotFoundException extends RuntimeException {
    public ApplicationUserNotFoundException(Long id) {
        super("Could not find application user " + id);
    }
}
