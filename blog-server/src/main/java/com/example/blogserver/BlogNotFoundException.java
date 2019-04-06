package com.example.blogserver;

public class BlogNotFoundException extends RuntimeException {
    BlogNotFoundException(Long id) {
        super("Could not find blog " + id);
    }
}
