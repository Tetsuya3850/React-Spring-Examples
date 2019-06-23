package com.example.instagramserver;

import com.example.instagramserver.commons.TestUtils;
import com.example.instagramserver.person.FormPerson;
import com.example.instagramserver.person.Person;
import com.example.instagramserver.person.SecurityConstants;
import com.example.instagramserver.post.FormPost;
import com.example.instagramserver.post.Post;
import com.example.instagramserver.tag.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.example.instagramserver.commons.TestConstants.*;
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
        FormPerson formPerson = new FormPerson(PERSON_ID, USERNAME, PASSWORD);
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

        // savePost
        FormPost formPost = new FormPost(POST_ID, POST_DESCRIPTION, Arrays.asList(TAG_TEXT_1, TAG_TEXT_2));

        HttpEntity<Object> savePostHttpEntity = new HttpEntity<>(formPost, headers);
        ResponseEntity<Post> savePostResponse = restTemplate.exchange(
                "/posts", HttpMethod.POST, savePostHttpEntity, Post.class);
        TestUtils.threadSleep(1000);
        assertEquals(HttpStatus.OK, savePostResponse.getStatusCode());
        assertEquals(POST_DESCRIPTION, savePostResponse.getBody().getDescription());
        assertEquals(TAG_TEXT_1, savePostResponse.getBody().getTags().get(0).getText());
        assertEquals(TAG_TEXT_2, savePostResponse.getBody().getTags().get(1).getText());
        assertEquals(USERNAME, savePostResponse.getBody().getPerson().getUsername());
        assertNull(savePostResponse.getBody().getPerson().getPassword());
        String postId = String.valueOf(savePostResponse.getBody().getId());

        // Add Seed Data
        FormPerson anotherPerson = new FormPerson(ANOTHER_PERSON_ID, ANOTHER_USERNAME, PASSWORD);
        restTemplate.postForEntity("/persons/signup", anotherPerson, Person.class);
        ResponseEntity<String> loginResponseAnother = restTemplate.postForEntity("/login", anotherPerson, String.class);
        String tokenAnother = loginResponseAnother.getBody();
        HttpHeaders headersAnother = new HttpHeaders();
        headersAnother.setContentType(MediaType.APPLICATION_JSON);
        headersAnother.set(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + tokenAnother);
        restTemplate.postForEntity("/persons/signup", anotherPerson, Person.class);
        restTemplate.exchange(
                "/posts", HttpMethod.POST, new HttpEntity<>(new FormPost(ANOTHER_POST_ID, ANOTHER_POST_DESCRIPTION, Arrays.asList(TAG_TEXT_2, TAG_TEXT_3)), headersAnother), Post.class);

        // Find Feed
        ResponseEntity<List<Post>> findFeedResponse = restTemplate.exchange(
                "/posts", HttpMethod.GET, baseHttpEntity, new ParameterizedTypeReference<List<Post>>() {});
        assertEquals(HttpStatus.OK, findFeedResponse.getStatusCode());
        assertEquals(2, findFeedResponse.getBody().size());
        assertEquals(ANOTHER_POST_DESCRIPTION, findFeedResponse.getBody().get(0).getDescription());

        // Person feed
        ResponseEntity<List<Post>> getPersonFeedResponse = restTemplate.exchange(
                "/posts/persons/{personId}", HttpMethod.GET, baseHttpEntity, new ParameterizedTypeReference<List<Post>>() {}, personId);
        assertEquals(HttpStatus.OK, getPersonFeedResponse.getStatusCode());
        assertEquals(1, getPersonFeedResponse.getBody().size());
        assertEquals(POST_DESCRIPTION, getPersonFeedResponse.getBody().get(0).getDescription());

        // Tag feed
        ResponseEntity<Tag> getTagFeedResponse = restTemplate.exchange(
                "/posts/tags/{tagText}", HttpMethod.GET, baseHttpEntity, Tag.class, TAG_TEXT_1);
        assertEquals(HttpStatus.OK, getTagFeedResponse.getStatusCode());
        assertEquals(TAG_TEXT_1, getTagFeedResponse.getBody().getText());
        assertEquals(1, getTagFeedResponse.getBody().getPosts().size());
        assertEquals(POST_DESCRIPTION, getTagFeedResponse.getBody().getPosts().get(0).getDescription());
        getTagFeedResponse = restTemplate.exchange(
                "/posts/tags/{tagText}", HttpMethod.GET, baseHttpEntity, Tag.class, TAG_TEXT_2);
        assertEquals(HttpStatus.OK, getTagFeedResponse.getStatusCode());
        assertEquals(2, getTagFeedResponse.getBody().getPosts().size());
        getTagFeedResponse = restTemplate.exchange(
                "/posts/tags/{tagText}", HttpMethod.GET, baseHttpEntity, Tag.class, TAG_TEXT_3);
        assertEquals(HttpStatus.OK, getTagFeedResponse.getStatusCode());
        assertEquals(ANOTHER_POST_DESCRIPTION, getTagFeedResponse.getBody().getPosts().get(0).getDescription());

        // Get Post
        ResponseEntity<Post> getPostResponse = restTemplate.exchange(
                "/posts/{postId}", HttpMethod.GET, baseHttpEntity, Post.class, postId);
        assertEquals(HttpStatus.OK, getPostResponse.getStatusCode());
        assertEquals(POST_DESCRIPTION, getPostResponse.getBody().getDescription());
        assertEquals(TAG_TEXT_1, getPostResponse.getBody().getTags().get(0).getText());
        assertEquals(TAG_TEXT_2, getPostResponse.getBody().getTags().get(1).getText());

        // Delete post fails for non-owner person, and succeeds for owner person
        ResponseEntity<Void> deletePostResponse = restTemplate.exchange(
                "/posts/{postId}", HttpMethod.DELETE, new HttpEntity<>(headersAnother), Void.class, postId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, deletePostResponse.getStatusCode());
        deletePostResponse = restTemplate.exchange(
                "/posts/{postId}", HttpMethod.DELETE, baseHttpEntity, Void.class, postId);
        assertEquals(HttpStatus.OK, deletePostResponse.getStatusCode());

    }

}

