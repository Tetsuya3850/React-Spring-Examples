package com.example.authserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static com.example.authserver.TestConstants.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class IntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void test(){
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
        String personId = String.valueOf(jsonPayload.get("id"));

        // getPerson Fail Without JWT
        ResponseEntity<Person> getPersonByIdWithoutJWTResponse = restTemplate.exchange(
                "/persons/{personId}", HttpMethod.GET, null, Person.class, personId);
        assertEquals(HttpStatus.FORBIDDEN, getPersonByIdWithoutJWTResponse.getStatusCode());

        // JWT header setup
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
        HttpEntity<String> baseEntity = new HttpEntity<>(headers);

        // getPersonById
        ResponseEntity<Person> getPersonByIdWithJWTResponse = restTemplate.exchange(
                "/persons/{personId}", HttpMethod.GET, baseEntity, Person.class, personId);
        assertEquals(HttpStatus.OK, getPersonByIdWithJWTResponse.getStatusCode());
        assertEquals(USERNAME, getPersonByIdWithJWTResponse.getBody().getUsername());
        assertNull(getPersonByIdWithJWTResponse.getBody().getPassword());
    }
}
