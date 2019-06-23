package com.example.instagramserver.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PersonRepository personRepository;

    @Autowired
    public UserDetailsServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new User(person.getUsername(), person.getPassword(), emptyList());
    }

    public Long loadIdByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return person.getId();
    }
}