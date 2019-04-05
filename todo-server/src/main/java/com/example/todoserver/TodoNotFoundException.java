package com.example.todoserver;

public class TodoNotFoundException extends RuntimeException {

    TodoNotFoundException(Long id) {
        super("Could not find todo " + id);
    }

}
