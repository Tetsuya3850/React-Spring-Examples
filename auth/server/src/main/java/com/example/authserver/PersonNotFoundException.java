package com.example.authserver;

public class PersonNotFoundException extends RuntimeException {

    PersonNotFoundException(Long id) {
        super("Could not find person " + id);
    }

}
