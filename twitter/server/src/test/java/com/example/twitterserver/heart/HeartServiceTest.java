package com.example.twitterserver.heart;

import com.example.twitterserver.person.Person;
import com.example.twitterserver.person.PersonRepository;
import com.example.twitterserver.tweet.Tweet;
import com.example.twitterserver.tweet.TweetRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.twitterserver.commons.TestConstants.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HeartServiceTest {

    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private HeartRepository heartRepository;

    private HeartService heartService;

    private Tweet mockTweet;
    private Person mockPerson;
    private Authentication mockAuth;
    private Heart mockHeart;

    @Before
    public void setup(){
        heartService = new HeartService(heartRepository, personRepository, tweetRepository);
        mockTweet =  new Tweet(TWEET_TEXT);
        mockPerson = new Person(USERNAME, PASSWORD);
        mockAuth = new UsernamePasswordAuthenticationToken(
                mockPerson.getUsername(), mockPerson.getPassword(), new ArrayList<>());
        mockHeart = new Heart(mockPerson, mockTweet);
    }

    @Test
    public void toggleHeart_saveHeart(){
        when(personRepository.findByUsername(USERNAME)).thenReturn(Optional.of(mockPerson));
        when(tweetRepository.findById(TWEET_ID)).thenReturn(Optional.of(mockTweet));
        when(heartRepository.findByPersonAndTweet(mockPerson, mockTweet)).thenReturn(null);

        heartService.toggleHeart(TWEET_ID, mockAuth);

        verify(heartRepository, times(1)).save(any(Heart.class));
        verify(tweetRepository, times(1)).save(mockTweet);
    }

    @Test
    public void toggleHeart_deleteHeart(){
        when(personRepository.findByUsername(USERNAME)).thenReturn(Optional.of(mockPerson));
        when(tweetRepository.findById(TWEET_ID)).thenReturn(Optional.of(mockTweet));
        when(heartRepository.findByPersonAndTweet(mockPerson, mockTweet)).thenReturn(mockHeart);

        heartService.toggleHeart(TWEET_ID, mockAuth);

        verify(heartRepository, times(1)).delete(mockHeart);
        verify(tweetRepository, times(1)).save(mockTweet);
    }

    @Test
    public void getAllHeartedPersonsByTweetId_Success(){
        List<Person> mockPersonList = new ArrayList<>();
        when(heartRepository.getAllHeartedPersonsByTweetId(TWEET_ID)).thenReturn(mockPersonList);

        List<Person> personList = heartService.getAllHeartedPersonsByTweetId(TWEET_ID);

        assertEquals(mockPersonList, personList);
        verify(heartRepository, times(1)).getAllHeartedPersonsByTweetId(TWEET_ID);
    }

    @Test
    public void getAllHeartedTweetsByPersonId_Success(){
        List<Tweet> mockTweetList = new ArrayList<>();
        when(heartRepository.getAllHeartedTweetsByPersonId(PERSON_ID)).thenReturn(mockTweetList);

        List<Tweet> tweetList = heartService.getAllHeartedTweetsByPersonId(PERSON_ID);

        assertEquals(mockTweetList, tweetList);
        verify(heartRepository, times(1)).getAllHeartedTweetsByPersonId(PERSON_ID);
    }

}
