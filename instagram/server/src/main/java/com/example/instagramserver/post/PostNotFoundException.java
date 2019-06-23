package com.example.instagramserver.post;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(Long id) {
        super("Could not find article " + id);
    }
}
