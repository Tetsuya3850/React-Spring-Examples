package com.example.authserver;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService){
        this.personService = personService;
    }

    @PostMapping("/signup")
    public Person savePerson(@Valid @RequestBody Person person) {
        return personService.savePerson(person);
    }

    @GetMapping("/{id}")
    Person getPerson(@PathVariable Long id) {
        return personService.findPersonById(id);
    }
}