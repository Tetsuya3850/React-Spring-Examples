package com.example.authserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import javax.persistence.PersistenceException;
import static org.junit.Assert.assertEquals;

@DataJpaTest
@RunWith(SpringRunner.class)
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private final String username = "me@gmail.com";
    private final String password = "Test3850";

    @Test(expected = PersistenceException.class)
    public void duplicateEmail_ThrowsException() {
        testEntityManager.persistAndFlush(new Person(username, password));
        testEntityManager.persistAndFlush(new Person(username, password));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void findByUsername_WithNonExistentUsername_ThrowsException() {
        testEntityManager.persistAndFlush(new Person(username, password));
        String nonExistUsername = "NONEXISTUSERNAME";
        personRepository
                .findByUsername(nonExistUsername)
                .orElseThrow(() -> new UsernameNotFoundException(nonExistUsername));
    }

    @Test
    public void findByUsername_ReturnsUser() throws Exception {
        testEntityManager.persistAndFlush(new Person(username, password));
        Person person = personRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        assertEquals(username, person.getUsername());
    }

}
