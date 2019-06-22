package com.example.twitterserver.heart;

import com.example.twitterserver.person.Person;
import com.example.twitterserver.tweet.Tweet;
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
public class HeartRepositoryTest {

    @Autowired
    private HeartRepository heartRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Person person;
    private Long person1Id;
    private Person another_person;
    private Tweet tweet1;
    private Long tweet1Id;
    private Tweet tweet2;

    @Before
    public void setup(){
        person = testEntityManager.persistFlushFind(new Person(USERNAME, PASSWORD));
        another_person = testEntityManager.persistFlushFind(new Person(ANOTHER_USERNAME, PASSWORD));
        person1Id = person.getId();
        tweet1 = new Tweet(TWEET_TEXT);
        tweet1.setPerson(person);
        tweet1 = testEntityManager.persistFlushFind(tweet1);
        tweet1Id = tweet1.getId();
        tweet2 = new Tweet(TWEET_TEXT);
        tweet2.setPerson(another_person);
        tweet2 = testEntityManager.persistFlushFind(tweet2);
        testEntityManager.persistAndFlush(new Heart(person, tweet1));
        testEntityManager.persistAndFlush(new Heart(another_person, tweet2));
    }

    @Test(expected = PersistenceException.class)
    public void duplicateHeart_ThrowsException(){
        testEntityManager.persistAndFlush(new Heart(person, tweet1));
    }

    @Test
    public void findByPersonAndTweet_Success(){
        Heart heart = heartRepository.findByPersonAndTweet(person, tweet1);
        assertEquals(person, heart.getPerson());
        assertEquals(tweet1, heart.getTweet());
        Heart heart_null = heartRepository.findByPersonAndTweet(another_person, tweet1);
        assertNull(heart_null);
    }

    @Test
    public void removeByTweet_Success(){
        heartRepository.removeByTweet(tweet1);
        List<Heart> heartList = heartRepository.findAll();
        assertEquals(1, heartList.size());
    }

    @Test
    public void getAllHeartedPersonsByTweetId_Success(){
        List<Person> personList = heartRepository.getAllHeartedPersonsByTweetId(tweet1Id);
        assertEquals(person, personList.get(0));
    }

    @Test
    public void getAllHeartedTweetsByPersonId_Success(){
        List<Tweet> tweetList = heartRepository.getAllHeartedTweetsByPersonId(person1Id);
        assertEquals(tweet1, tweetList.get(0));
    }

    @Test
    public void getAllHeartedTweetIdsByPerson_Success(){
        List<Long> tweetIdList = heartRepository.getAllHeartedTweetIdsByPerson(person);
        assertEquals(tweet1.getId(), tweetIdList.get(0));
    }

}
