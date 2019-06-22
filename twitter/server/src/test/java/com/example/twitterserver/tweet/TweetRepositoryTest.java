package com.example.twitterserver.tweet;

import com.example.twitterserver.follow.Follow;
import com.example.twitterserver.person.Person;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.example.twitterserver.commons.TestConstants.*;
import static org.junit.Assert.assertEquals;

@DataJpaTest
@RunWith(SpringRunner.class)
public class TweetRepositoryTest {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Person person;
    private Person another_person;
    private Person no_tweet_person;
    private Tweet tweet1;
    private Long tweet1Id;
    private Tweet tweet2;
    private Tweet tweet3;

    @Before
    public void setup(){
        person = testEntityManager.persistFlushFind(new Person(USERNAME, PASSWORD));
        another_person = testEntityManager.persistFlushFind(new Person(ANOTHER_USERNAME, PASSWORD));
        no_tweet_person = testEntityManager.persistFlushFind(new Person("no@gmail.com", PASSWORD));
        tweet1 = new Tweet(TWEET_TEXT);
        tweet1.setPerson(person);
        tweet1 = testEntityManager.persistFlushFind(tweet1);
        tweet1Id = tweet1.getId();
        tweet2 = new Tweet(TWEET_TEXT);
        tweet2.setPerson(another_person);
        tweet2 = testEntityManager.persistFlushFind(tweet2);
        tweet3 = new Tweet(TWEET_TEXT);
        tweet3.setPerson(another_person);
        tweet3 = testEntityManager.persistFlushFind(tweet3);
        testEntityManager.persistAndFlush(new Follow(person, another_person));
    }

    @Test
    public void getFeed_Success(){
        List<Tweet> tweetList = tweetRepository.getFeed(person.getId());
        assertEquals(3, tweetList.size());
        assertEquals(tweet3, tweetList.get(0));
        List<Tweet> tweetListAnotherPerson = tweetRepository.getFeed(another_person.getId());
        assertEquals(2, tweetListAnotherPerson.size());
        assertEquals(tweet3, tweetListAnotherPerson.get(0));
    }

    @Test
    public void findByPersonOrderByCreatedDesc_Success(){
        List<Tweet> tweetList = tweetRepository.findByPersonOrderByCreatedDesc(person);
        assertEquals(1, tweetList.size());
        assertEquals(tweet1, tweetList.get(0));
        List<Tweet> tweetListAnother = tweetRepository.findByPersonOrderByCreatedDesc(another_person);
        assertEquals(2, tweetListAnother.size());
        assertEquals(tweet3, tweetListAnother.get(0));
    }

    @Test
    public void deleteByIdAndPerson_WithNonOwnerPerson_DeletesNothing(){
        long result = tweetRepository.deleteByIdAndPerson(tweet1Id, no_tweet_person);
        assertEquals(0L, result);
    }

    @Test
    public void deleteByIdAndPerson_Success(){
        long result = tweetRepository.deleteByIdAndPerson(tweet1Id, person);
        assertEquals(1L, result);
    }

}
