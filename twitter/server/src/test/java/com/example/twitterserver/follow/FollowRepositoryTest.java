package com.example.twitterserver.follow;

import com.example.twitterserver.person.Person;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.PersistenceException;
import java.util.List;

import static com.example.twitterserver.commons.TestConstants.*;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;

@DataJpaTest
@RunWith(SpringRunner.class)
public class FollowRepositoryTest {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Person person;
    private Person another_person;

    @Before
    public void setup(){
        person = testEntityManager.persistFlushFind(new Person(USERNAME, PASSWORD));
        another_person = testEntityManager.persistFlushFind(new Person(ANOTHER_USERNAME, PASSWORD));
        testEntityManager.persistAndFlush(new Follow(person, another_person));
    }

    @Test(expected = PersistenceException.class)
    public void duplicateFollow_ThrowsException(){
        testEntityManager.persistAndFlush(new Follow(person, another_person));
    }

    @Test
    public void findByFollowerAndFollowee_Success(){
        Follow follow = followRepository.findByFollowerAndFollowee(person, another_person);
        assertEquals(person, follow.getFollower());
        assertEquals(another_person, follow.getFollowee());
        Follow follow_null = followRepository.findByFollowerAndFollowee(another_person, person);
        assertNull(follow_null);
    }

    @Test
    public void getAllFollowingByPersonId_Success(){
        List<Person> personList = followRepository.getAllFollowingByPersonId(person.getId());
        assertEquals(another_person, personList.get(0));
        List<Person> personListAnother = followRepository.getAllFollowingByPersonId(another_person.getId());
        assertEquals(0, personListAnother.size());
    }

    @Test
    public void getAllFollowersByPersonId_Success(){
        List<Person> personList = followRepository.getAllFollowersByPersonId(person.getId());
        assertEquals(0, personList.size());
        List<Person> personListAnother = followRepository.getAllFollowersByPersonId(another_person.getId());
        assertEquals(person, personListAnother.get(0));
    }

    @Test
    public void getAllFollowingPersonIdsByPerson_Success(){
        List<Long> personIdList = followRepository.getAllFollowingPersonIdsByPerson(person);
        assertEquals(another_person.getId(), personIdList.get(0));
    }
}
