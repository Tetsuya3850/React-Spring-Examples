package com.example.blogserver;

import com.example.blogserver.article.Article;
import com.example.blogserver.article.FormArticle;
import com.example.blogserver.commons.RestPageImpl;
import com.example.blogserver.commons.TestUtils;
import com.example.blogserver.person.FormPerson;
import com.example.blogserver.person.Person;
import com.example.blogserver.person.SecurityConstants;
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

import static com.example.blogserver.commons.TestConstants.*;
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

        // Login
        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/login", formPerson, String.class);
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        String token = loginResponse.getBody();
        Map jsonPayload = TestUtils.decodeJWTPayload(token);
        assertEquals(jsonPayload.get("sub"), USERNAME);
        String person1Id = String.valueOf(jsonPayload.get("id"));

        // getPersonById Fail Without JWT
        ResponseEntity<Person> getPersonByIdWithoutJWTResponse = restTemplate.exchange(
                "/persons/{personId}", HttpMethod.GET, null, Person.class, person1Id);
        assertEquals(HttpStatus.FORBIDDEN, getPersonByIdWithoutJWTResponse.getStatusCode());

        // JWT header setup
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
        HttpEntity<String> baseHttpEntity = new HttpEntity<>(headers);

        // getPersonById
        ResponseEntity<Person> getPersonByIdWithJWTResponse = restTemplate.exchange(
                "/persons/{personId}", HttpMethod.GET, baseHttpEntity, Person.class, person1Id);
        assertEquals(HttpStatus.OK, getPersonByIdWithJWTResponse.getStatusCode());
        assertEquals(USERNAME, getPersonByIdWithJWTResponse.getBody().getUsername());
        assertNull(getPersonByIdWithJWTResponse.getBody().getPassword());

        // saveArticle
        String ARTICLE_TITLE_1 = ARTICLE_TITLE + "_1";
        FormArticle formArticle1 = new FormArticle(ARTICLE_TITLE_1, ARTICLE_TEXT);
        HttpEntity<Object> saveArticleHttpEntity = new HttpEntity<>(formArticle1, headers);
        ResponseEntity<Article> saveArticleResponse = restTemplate.exchange(
                "/articles", HttpMethod.POST, saveArticleHttpEntity, Article.class);
        TestUtils.threadSleep(1000);
        assertEquals(HttpStatus.OK, saveArticleResponse.getStatusCode());
        assertEquals(ARTICLE_TITLE_1, saveArticleResponse.getBody().getTitle());
        assertEquals(USERNAME, saveArticleResponse.getBody().getPerson().getUsername());
        assertNull(saveArticleResponse.getBody().getPerson().getPassword());
        String article1Id = String.valueOf(saveArticleResponse.getBody().getId());

        // Add Seed Data
        String ARTICLE_TITLE_2 = ARTICLE_TITLE + "_2";
        restTemplate.exchange("/articles", HttpMethod.POST, new HttpEntity<>(new FormArticle(ARTICLE_TITLE_2, ARTICLE_TEXT), headers), Article.class);
        TestUtils.threadSleep(1000);
        String USERNAME_2 = USERNAME + "_2";
        FormPerson formPerson2 = new FormPerson(USERNAME_2, PASSWORD);
        restTemplate.postForEntity("/persons/signup", formPerson2, Person.class);
        ResponseEntity<String> loginResponse2 = restTemplate.postForEntity("/login", formPerson2, String.class);
        String token2 = loginResponse2.getBody();
        HttpHeaders headers2 = new HttpHeaders();
        headers2.setContentType(MediaType.APPLICATION_JSON);
        headers2.set(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token2);
        String ARTICLE_TITLE_3 = ARTICLE_TITLE + "_3";
        restTemplate.exchange("/articles", HttpMethod.POST, new HttpEntity<>(
                new FormArticle(ARTICLE_TITLE_3, ARTICLE_TEXT), headers2), Article.class);
        TestUtils.threadSleep(1000);
        String ARTICLE_TITLE_4 = ARTICLE_TITLE + "_4";
        restTemplate.exchange("/articles", HttpMethod.POST, new HttpEntity<>(
                new FormArticle(ARTICLE_TITLE_4, ARTICLE_TEXT), headers2), Article.class);

        // Feed first page
        ResponseEntity<RestPageImpl<Article>> getFeedResponse = restTemplate.exchange(
                "/articles", HttpMethod.GET, baseHttpEntity, new ParameterizedTypeReference<RestPageImpl<Article>>() {});
        assertEquals(HttpStatus.OK, getFeedResponse.getStatusCode());
        assertEquals(ARTICLE_TITLE_4, getFeedResponse.getBody().getContent().get(0).getTitle());
        assertEquals(USERNAME_2, getFeedResponse.getBody().getContent().get(0).getPerson().getUsername());

        // Feed next page
        ResponseEntity<RestPageImpl<Article>> getNextPageFeedResponse = restTemplate.exchange(
                "/articles?page={page}", HttpMethod.GET, baseHttpEntity, new ParameterizedTypeReference<RestPageImpl<Article>>() {}, 1);
        assertEquals(HttpStatus.OK, getNextPageFeedResponse.getStatusCode());
        assertEquals(ARTICLE_TITLE_1, getNextPageFeedResponse.getBody().getContent().get(0).getTitle());
        assertEquals(USERNAME, getNextPageFeedResponse.getBody().getContent().get(0).getPerson().getUsername());

        // User articles
        ResponseEntity<List<Article>> getPersonArticlesResponse = restTemplate.exchange(
                "/articles/persons/{personId}", HttpMethod.GET, baseHttpEntity, new ParameterizedTypeReference<List<Article>>() {}, person1Id);
        assertEquals(HttpStatus.OK, getPersonArticlesResponse.getStatusCode());
        assertEquals(2, getPersonArticlesResponse.getBody().size());
        assertEquals(ARTICLE_TITLE_2, getPersonArticlesResponse.getBody().get(0).getTitle());
        assertEquals(USERNAME, getPersonArticlesResponse.getBody().get(0).getPerson().getUsername());

        // Get article
        ResponseEntity<Article> getArticleResponse = restTemplate.exchange(
                "/articles/{articleId}", HttpMethod.GET, baseHttpEntity, Article.class, article1Id);
        assertEquals(HttpStatus.OK, getArticleResponse.getStatusCode());
        assertEquals(ARTICLE_TITLE_1, getArticleResponse.getBody().getTitle());
        assertEquals(USERNAME, getArticleResponse.getBody().getPerson().getUsername());
        assertNull(getArticleResponse.getBody().getPerson().getPassword());

        // Edit article
        String EDIT_TITLE = "EDIT_TITLE";
        FormArticle editArticle = new FormArticle(EDIT_TITLE, ARTICLE_TEXT);
        HttpEntity<Object> editArticleHttpEntity = new HttpEntity<>(editArticle, headers);
        ResponseEntity<Article> editArticleResponse = restTemplate.exchange(
                "/articles/{articleId}", HttpMethod.PUT, editArticleHttpEntity, Article.class, article1Id);
        assertEquals(HttpStatus.OK, editArticleResponse.getStatusCode());
        assertEquals(EDIT_TITLE, editArticleResponse.getBody().getTitle());

        // Delete article
        ResponseEntity<Void> deleteArticleResponse = restTemplate.exchange(
                "/articles/{articleId}", HttpMethod.DELETE, baseHttpEntity, Void.class, article1Id);
        assertEquals(HttpStatus.OK, deleteArticleResponse.getStatusCode());
        ResponseEntity<List<Article>> getPersonArticlesAgainResponse = restTemplate.exchange(
                "/articles/persons/{personId}", HttpMethod.GET, baseHttpEntity, new ParameterizedTypeReference<List<Article>>() {}, person1Id);
        assertEquals(HttpStatus.OK, getPersonArticlesAgainResponse.getStatusCode());
        assertEquals(1, getPersonArticlesAgainResponse.getBody().size());
    }
}