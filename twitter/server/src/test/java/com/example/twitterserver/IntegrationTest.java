package com.example.twitterserver;

import com.example.twitterserver.commons.TestUtils;
import com.example.twitterserver.person.FormPerson;
import com.example.twitterserver.person.OwnInfo;
import com.example.twitterserver.person.Person;
import com.example.twitterserver.person.SecurityConstants;
import com.example.twitterserver.tweet.FormTweet;
import com.example.twitterserver.tweet.Tweet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import static com.example.twitterserver.commons.TestConstants.*;
import static com.example.twitterserver.commons.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class IntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void test() {
        // Signup
        FormPerson formPerson = new FormPerson(USERNAME, PASSWORD);
        ResponseEntity<Person> signupResponse = restTemplate.postForEntity("/persons/signup", formPerson, Person.class);
        assertEquals(HttpStatus.OK, signupResponse.getStatusCode());
        assertEquals(USERNAME, signupResponse.getBody().getUsername());
        assertNull(signupResponse.getBody().getPassword());
        String personId = String.valueOf(signupResponse.getBody().getId());

        // Login
        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/login", formPerson, String.class);
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        String token = loginResponse.getBody();
        Map jsonPayload = TestUtils.decodeJWTPayload(token);
        assertEquals(USERNAME, jsonPayload.get("sub"));
        assertEquals(personId, jsonPayload.get("id"));

        // getPersonById Fail Without JWT
        ResponseEntity<Person> getPersonByIdWithoutJWTResponse = restTemplate.exchange(
                "/persons/{personId}", HttpMethod.GET, null, Person.class, personId);
        assertEquals(HttpStatus.FORBIDDEN, getPersonByIdWithoutJWTResponse.getStatusCode());

        // JWT header setup
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
        HttpEntity<String> baseHttpEntity = new HttpEntity<>(headers);

        // getPersonById
        ResponseEntity<Person> getPersonByIdWithJWTResponse = restTemplate.exchange(
                "/persons/{personId}", HttpMethod.GET, baseHttpEntity, Person.class, personId);
        assertEquals(HttpStatus.OK, getPersonByIdWithJWTResponse.getStatusCode());
        assertEquals(USERNAME, getPersonByIdWithJWTResponse.getBody().getUsername());
        assertNull(getPersonByIdWithJWTResponse.getBody().getPassword());

        // saveTweet
        String TWEET_TEXT_1 = TWEET_TEXT + "_1";
        FormTweet formTweet1 = new FormTweet(TWEET_TEXT_1);
        HttpEntity<Object> saveTweetHttpEntity = new HttpEntity<>(formTweet1, headers);
        ResponseEntity<Tweet> saveTweetResponse = restTemplate.exchange(
                "/tweets", HttpMethod.POST, saveTweetHttpEntity, Tweet.class);
        TestUtils.threadSleep(1000);
        assertEquals(HttpStatus.OK, saveTweetResponse.getStatusCode());
        assertEquals(TWEET_TEXT_1, saveTweetResponse.getBody().getText());
        assertEquals(USERNAME, saveTweetResponse.getBody().getPerson().getUsername());
        assertNull(saveTweetResponse.getBody().getPerson().getPassword());
        String tweet1Id = String.valueOf(saveTweetResponse.getBody().getId());

        // Add Seed Data
        FormPerson anotherPerson = new FormPerson(ANOTHER_USERNAME, PASSWORD);
        restTemplate.postForEntity("/persons/signup", anotherPerson, Person.class);
        ResponseEntity<String> loginResponseAnother = restTemplate.postForEntity("/login", anotherPerson, String.class);
        String tokenAnother = loginResponseAnother.getBody();
        String anotherPersonId = String.valueOf(decodeJWTPayload(tokenAnother).get("id"));
        HttpHeaders headersAnother = new HttpHeaders();
        headersAnother.setContentType(MediaType.APPLICATION_JSON);
        headersAnother.set(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + tokenAnother);
        String TWEET_TEXT_2 = TWEET_TEXT + "_2";
        restTemplate.exchange(
                "/tweets", HttpMethod.POST, new HttpEntity<>(new FormTweet(TWEET_TEXT_2), headersAnother), Tweet.class);

        // Get Feed Returns only own tweet
        ResponseEntity<List<Tweet>> getFeedResponse = restTemplate.exchange(
                "/tweets", HttpMethod.GET, baseHttpEntity, new ParameterizedTypeReference<List<Tweet>>() {});
        assertEquals(HttpStatus.OK, getFeedResponse.getStatusCode());
        assertEquals(1, getFeedResponse.getBody().size());
        assertEquals(TWEET_TEXT_1, getFeedResponse.getBody().get(0).getText());
        assertEquals(USERNAME, getFeedResponse.getBody().get(0).getPerson().getUsername());

        // findAllPersons
        ResponseEntity<List<Person>> getAllPersonsResponse = restTemplate.exchange(
                "/persons", HttpMethod.GET, baseHttpEntity, new ParameterizedTypeReference<List<Person>>() {});
        assertEquals(HttpStatus.OK, getAllPersonsResponse.getStatusCode());
        assertEquals(2, getAllPersonsResponse.getBody().size());

        // person follow another_person
        restTemplate.exchange(
                "/follows/{personId}", HttpMethod.POST, baseHttpEntity, Void.class, anotherPersonId);

        // person is following another_person
        ResponseEntity<List<Person>> followingResponse = restTemplate.exchange(
                "/follows/following/{personId}", HttpMethod.GET, baseHttpEntity, new ParameterizedTypeReference<List<Person>>() {}, personId);
        assertEquals(HttpStatus.OK, followingResponse.getStatusCode());
        assertEquals(ANOTHER_USERNAME, followingResponse.getBody().get(0).getUsername());
        assertEquals(1, followingResponse.getBody().get(0).getFollowersCount());
        assertEquals(0, followingResponse.getBody().get(0).getFollowingCount());

        // another_person is followed by person
        ResponseEntity<List<Person>> followersResponse = restTemplate.exchange(
                "/follows/followers/{personId}", HttpMethod.GET, baseHttpEntity, new ParameterizedTypeReference<List<Person>>() {}, anotherPersonId);
        assertEquals(HttpStatus.OK, followersResponse.getStatusCode());
        assertEquals(USERNAME, followersResponse.getBody().get(0).getUsername());
        assertEquals(0, followersResponse.getBody().get(0).getFollowersCount());
        assertEquals(1, followersResponse.getBody().get(0).getFollowingCount());

        // Get Feed Returns own and following persons tweet
        ResponseEntity<List<Tweet>> getFeedResponseAgain = restTemplate.exchange(
                "/tweets", HttpMethod.GET, baseHttpEntity, new ParameterizedTypeReference<List<Tweet>>() {});
        assertEquals(HttpStatus.OK, getFeedResponseAgain.getStatusCode());
        assertEquals(2, getFeedResponseAgain.getBody().size());
        assertEquals(TWEET_TEXT_2, getFeedResponseAgain.getBody().get(0).getText());
        assertEquals(ANOTHER_USERNAME, getFeedResponseAgain.getBody().get(0).getPerson().getUsername());

        // Person feed
        ResponseEntity<List<Tweet>> getPersonFeedResponse = restTemplate.exchange(
                "/tweets/persons/{personId}", HttpMethod.GET, baseHttpEntity, new ParameterizedTypeReference<List<Tweet>>() {}, anotherPersonId);
        assertEquals(HttpStatus.OK, getPersonFeedResponse.getStatusCode());
        assertEquals(1, getPersonFeedResponse.getBody().size());
        assertEquals(TWEET_TEXT_2, getPersonFeedResponse.getBody().get(0).getText());
        assertEquals(ANOTHER_USERNAME, getPersonFeedResponse.getBody().get(0).getPerson().getUsername());

        // Get tweet
        ResponseEntity<Tweet> getTweetResponse = restTemplate.exchange(
                "/tweets/{tweetId}", HttpMethod.GET, baseHttpEntity, Tweet.class, tweet1Id);
        assertEquals(HttpStatus.OK, getTweetResponse.getStatusCode());
        assertEquals(TWEET_TEXT_1, getTweetResponse.getBody().getText());

        // Person hearts tweet1
        ResponseEntity<Void> heartTweetResponse = restTemplate.exchange(
                "/hearts/{tweetId}", HttpMethod.POST, baseHttpEntity, Void.class, tweet1Id);
        assertEquals(HttpStatus.OK, heartTweetResponse.getStatusCode());

        // person1 has hearted tweet1
        ResponseEntity<List<Tweet>> getHeartedTweetsReseponse = restTemplate.exchange(
                "/hearts/persons/{personId}", HttpMethod.GET, baseHttpEntity,  new ParameterizedTypeReference<List<Tweet>>() {}, personId);
        assertEquals(HttpStatus.OK, getHeartedTweetsReseponse.getStatusCode());
        assertEquals(TWEET_TEXT_1, getHeartedTweetsReseponse.getBody().get(0).getText());
        assertEquals(1, getHeartedTweetsReseponse.getBody().get(0).getHeartCount());

        // tweet1 was hearted by person1
        ResponseEntity<List<Person>> getHeartedPersonsResponse = restTemplate.exchange(
                "/hearts/tweets/{tweetId}", HttpMethod.GET, baseHttpEntity,  new ParameterizedTypeReference<List<Person>>() {}, tweet1Id);
        assertEquals(HttpStatus.OK, getHeartedPersonsResponse.getStatusCode());
        assertEquals(USERNAME, getHeartedPersonsResponse.getBody().get(0).getUsername());

        // person1 has hearted tweet1 and followed anotherPerson
        ResponseEntity<OwnInfo> getOwnInfoResponse = restTemplate.exchange(
                "/persons/me", HttpMethod.GET, baseHttpEntity, OwnInfo.class);
        assertEquals(HttpStatus.OK, getOwnInfoResponse.getStatusCode());
        assertEquals(Long.valueOf(tweet1Id), getOwnInfoResponse.getBody().getHeartedTweetIds().get(0));
        assertEquals(Long.valueOf(anotherPersonId), getOwnInfoResponse.getBody().getFollowingPersonIds().get(0));

        // Unheart tweet1 and unfollow anotherPerson
        restTemplate.exchange(
                "/hearts/{tweetId}", HttpMethod.POST, baseHttpEntity, Void.class, tweet1Id);
        restTemplate.exchange(
                "/follows/{personId}", HttpMethod.POST, baseHttpEntity, Void.class, anotherPersonId);

        // person1 has no hearted tweets and following persons
        ResponseEntity<OwnInfo> getOwnInfoResponseAgain = restTemplate.exchange(
                "/persons/me", HttpMethod.GET, baseHttpEntity, OwnInfo.class);
        assertEquals(0, getOwnInfoResponseAgain.getBody().getFollowingPersonIds().size());
        assertEquals(0, getOwnInfoResponseAgain.getBody().getHeartedTweetIds().size());

        // tweet1's heart count decremented
        ResponseEntity<Tweet> getTweetResponseAgain = restTemplate.exchange(
                "/tweets/{tweetId}", HttpMethod.GET, baseHttpEntity, Tweet.class, tweet1Id);
        assertEquals(0, getTweetResponseAgain.getBody().getHeartCount());

        // person's following count decremented. another_person's follower count decremented
        ResponseEntity<Person> getPersonByIdResponseAgain = restTemplate.exchange(
                "/persons/{personId}", HttpMethod.GET, baseHttpEntity, Person.class, personId);
        assertEquals(0, getPersonByIdResponseAgain.getBody().getFollowingCount());
        getPersonByIdResponseAgain = restTemplate.exchange(
                "/persons/{personId}", HttpMethod.GET, baseHttpEntity, Person.class, anotherPersonId);
        assertEquals(0, getPersonByIdResponseAgain.getBody().getFollowersCount());

        // Delete tweet1 fails for non-owner person, and succeeds for owner person
        ResponseEntity<Void> deleteTweetResponse = restTemplate.exchange(
                "/tweets/{tweetId}", HttpMethod.DELETE, new HttpEntity<>(headersAnother), Void.class, tweet1Id);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, deleteTweetResponse.getStatusCode());
        deleteTweetResponse = restTemplate.exchange(
                "/tweets/{tweetId}", HttpMethod.DELETE, baseHttpEntity, Void.class, tweet1Id);
        assertEquals(HttpStatus.OK, deleteTweetResponse.getStatusCode());
    }

}
