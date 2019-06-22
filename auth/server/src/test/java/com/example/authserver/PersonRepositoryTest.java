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
import static com.example.authserver.TestConstants.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test(expected = PersistenceException.class)
    public void duplicateUsername_ThrowsException() {
        testEntityManager.persistAndFlush(new Person(USERNAME, PASSWORD));
        testEntityManager.persistAndFlush(new Person(USERNAME, PASSWORD));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void findByUsername_WithNonExistentUsername_ThrowsException() {
        testEntityManager.persistAndFlush(new Person(USERNAME, PASSWORD));
        final String NON_EXIST_USERNAME = "no@gmail.com";
        personRepository
                .findByUsername(NON_EXIST_USERNAME)
                .orElseThrow(() -> new UsernameNotFoundException(NON_EXIST_USERNAME));
    }

    @Test
    public void findByUsername_Success() {
        testEntityManager.persistAndFlush(new Person(USERNAME, PASSWORD));
        Person person = personRepository
                .findByUsername(USERNAME)
                .orElseThrow(() -> new UsernameNotFoundException(USERNAME));
        assertEquals(USERNAME, person.getUsername());
    }

}
