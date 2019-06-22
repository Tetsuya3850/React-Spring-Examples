package com.example.twitterserver.tweet;

import com.auth0.jwt.JWT;
import com.example.twitterserver.commons.TestUtils;
import com.example.twitterserver.person.SecurityConstants;
import com.example.twitterserver.person.UserDetailsServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.example.twitterserver.commons.TestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TweetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TweetService tweetService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private String token;
    private Tweet mockTweet;

    @Before
    public void setup(){
        token = JWT.create()
                .withSubject(USERNAME)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .withClaim("id", Long.toString(PERSON_ID))
                .sign(HMAC512(SecurityConstants.JWT_SECRET.getBytes()));
        mockTweet = new Tweet(TWEET_TEXT);
        when(userDetailsService.loadUserByUsername(USERNAME)).thenReturn(new User(USERNAME, PASSWORD, new ArrayList()));
    }

    @Test
    public void saveTweet_WithInvalidTweet_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/tweets")
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token)
                .content(TestUtils.asJsonString(new FormTweet()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void saveTweet_Success() throws Exception {
        when(tweetService.saveTweet(any(Tweet.class), any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockTweet);

        mockMvc.perform(post("/tweets")
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token)
                .content(TestUtils.asJsonString(new FormTweet(TWEET_TEXT)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(TWEET_TEXT));

        verify(tweetService, times(1)).saveTweet(any(Tweet.class), any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void findFeed_Success() throws Exception {
        List<Tweet> mockTweetList = new ArrayList<>();
        when(tweetService.getFeed(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockTweetList);

        mockMvc.perform(get("/tweets/")
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(tweetService, times(1)).getFeed(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void findAllPersonTweets_Success() throws Exception {
        List<Tweet> mockTweetList = new ArrayList<>();
        when(tweetService.findAllPersonTweets(PERSON_ID)).thenReturn(mockTweetList);

        mockMvc.perform(get("/tweets/persons/{personId}", PERSON_ID)
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(tweetService, times(1)).findAllPersonTweets(PERSON_ID);
    }

    @Test(expected = NestedServletException.class)
    public void findTweetById_WithInvalidId_ThrowsException() throws Exception {
        doThrow(new IllegalArgumentException()).when(tweetService).findTweetById(TWEET_ID);

        mockMvc.perform(get("/tweets/{tweetId}", TWEET_ID)
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token));
    }

    @Test
    public void findTweetById_Success() throws Exception {
        when(tweetService.findTweetById(TWEET_ID)).thenReturn(mockTweet);

        mockMvc.perform(get("/tweets/{tweetId}", TWEET_ID)
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(TWEET_TEXT));

        verify(tweetService, times(1)).findTweetById(TWEET_ID);
    }

    @Test(expected = NestedServletException.class)
    public void deleteTweet_WithInvalidIdAndPerson_ThrowsException() throws Exception {
        doThrow(new IllegalArgumentException()).when(tweetService).deleteTweet(
                eq(TWEET_ID), any(UsernamePasswordAuthenticationToken.class));

        mockMvc.perform(delete("/tweets/{tweetId}", TWEET_ID)
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token));
    }

    @Test
    public void deleteTweet_Success() throws Exception {
        doNothing().when(tweetService).deleteTweet(
                eq(TWEET_ID), any(UsernamePasswordAuthenticationToken.class));

        mockMvc.perform(delete("/tweets/{tweetId}", TWEET_ID)
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token))
                .andExpect(status().isOk());

        verify(tweetService, times(1)).deleteTweet(
                eq(TWEET_ID), any(UsernamePasswordAuthenticationToken.class));
    }

}
