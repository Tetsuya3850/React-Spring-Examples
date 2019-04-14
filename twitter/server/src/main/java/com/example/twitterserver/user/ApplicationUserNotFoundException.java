package com.example.twitterserver.user;

public class ApplicationUserNotFoundException extends RuntimeException {
    public ApplicationUserNotFoundException(Long id) {
        super("Could not find application user " + id);
    }
}
