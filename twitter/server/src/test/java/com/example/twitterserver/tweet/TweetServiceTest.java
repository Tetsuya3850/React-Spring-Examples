package com.example.twitterserver.tweet;

import com.example.twitterserver.heart.Heart;
import com.example.twitterserver.heart.HeartRepository;
import com.example.twitterserver.person.Person;
import com.example.twitterserver.person.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.twitterserver.commons.TestConstants.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TweetServiceTest {

    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private HeartRepository heartRepository;

    private TweetService tweetService;

    private Tweet mockTweet;
    private Person mockPerson;
    private Authentication mockAuth;

    @Before
    public void setup(){
        tweetService = new TweetService(tweetRepository, personRepository, heartRepository);
        mockTweet =  new Tweet(TWEET_TEXT);
        mockPerson = new Person(USERNAME, PASSWORD);
        mockPerson.setId(PERSON_ID);
        mockAuth = new UsernamePasswordAuthenticationToken(
                mockPerson.getUsername(), mockPerson.getPassword(), new ArrayList<>());
    }

    @Test
    public void saveTweet_Success(){
        class IsTweetWithPerson implements ArgumentMatcher<Tweet> {
            public boolean matches(Tweet tweet) {
                return tweet.getPerson() != null;
            }
        }

        when(personRepository.findByUsername(mockPerson.getUsername())).thenReturn(Optional.of(mockPerson));
        when(tweetRepository.save(argThat(new IsTweetWithPerson()))).thenReturn(mockTweet);

        Tweet tweet = tweetService.saveTweet(mockTweet, mockAuth);

        assertEquals(mockTweet, tweet);
        verify(tweetRepository, times(1)).save(mockTweet);
    }

    @Test
    public void findFeed_Success(){
        List<Tweet> mockTweetList = new ArrayList<>();
        when(tweetRepository.getFeed(PERSON_ID)).thenReturn(mockTweetList);
        when(personRepository.findByUsername(USERNAME)).thenReturn(Optional.of(mockPerson));

        List<Tweet> tweetList = tweetService.getFeed(mockAuth);

        assertEquals(mockTweetList, tweetList);
        verify(tweetRepository, times(1)).getFeed(PERSON_ID);
    }

    @Test
    public void findAllPersonTweets_Success(){
        List<Tweet> mockTweetList = new ArrayList<>();
        when(personRepository.findById(PERSON_ID)).thenReturn(Optional.of(mockPerson));
        when(tweetRepository.findByPersonOrderByCreatedDesc(mockPerson)).thenReturn(mockTweetList);

        List<Tweet> tweetList = tweetService.findAllPersonTweets(PERSON_ID);

        assertEquals(mockTweetList, tweetList);
        verify(tweetRepository, times(1)).findByPersonOrderByCreatedDesc(mockPerson);
    }

    @Test(expected = TweetNotFoundException.class)
    public void findTweetById_WithInvalidId_ThrowsException(){
        doThrow(new TweetNotFoundException(TWEET_ID)).when(tweetRepository).findById(TWEET_ID);

        tweetService.findTweetById(TWEET_ID);
    }

    @Test
    public void findTweetById_Success(){
        when(tweetRepository.findById(TWEET_ID)).thenReturn(Optional.of(mockTweet));

        Tweet tweet = tweetService.findTweetById(TWEET_ID);

        assertEquals(mockTweet, tweet);
        verify(tweetRepository, times(1)).findById(TWEET_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteTweet_WithInvalidIdAndPerson_ThrowsException(){
        when(personRepository.findByUsername(mockPerson.getUsername())).thenReturn(Optional.of(mockPerson));
        when(tweetRepository.findById(TWEET_ID)).thenReturn(Optional.of(mockTweet));
        when(tweetRepository.deleteByIdAndPerson(TWEET_ID, mockPerson)).thenReturn(0L);
        when(heartRepository.removeByTweet(mockTweet)).thenReturn(new ArrayList<Heart>());

        tweetService.deleteTweet(TWEET_ID, mockAuth);
    }

    @Test
    public void deleteTweet_Success(){
        when(personRepository.findByUsername(mockPerson.getUsername())).thenReturn(Optional.of(mockPerson));
        when(tweetRepository.findById(TWEET_ID)).thenReturn(Optional.of(mockTweet));
        when(tweetRepository.deleteByIdAndPerson(TWEET_ID, mockPerson)).thenReturn(1L);
        when(heartRepository.removeByTweet(mockTweet)).thenReturn(new ArrayList<Heart>());

        tweetService.deleteTweet(TWEET_ID, mockAuth);

        verify(tweetRepository, times(1)).deleteByIdAndPerson(TWEET_ID, mockPerson);
        verify(heartRepository, times(1)).removeByTweet(mockTweet);
    }

}
