package com.example.blogserver;

public class ArticleNotFoundException extends RuntimeException {
    ArticleNotFoundException(Long id) {
        super("Could not find article " + id);
    }
}
