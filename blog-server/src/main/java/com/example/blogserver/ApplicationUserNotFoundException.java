package com.example.blogserver;

public class ApplicationUserNotFoundException extends RuntimeException {
    ApplicationUserNotFoundException(Long id) {
        super("Could not find application user " + id);
    }
}
