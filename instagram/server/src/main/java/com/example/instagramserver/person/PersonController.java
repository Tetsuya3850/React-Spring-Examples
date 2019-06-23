package com.example.instagramserver.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService){
        this.personService = personService;
    }

    @PostMapping("/signup")
    Person savePerson(@Valid @RequestBody Person person) {
        return personService.savePerson(person);
    }

    @GetMapping("/{personId}")
    Person findPersonById(@PathVariable Long personId) {
        return personService.findPersonById(personId);
    }
}