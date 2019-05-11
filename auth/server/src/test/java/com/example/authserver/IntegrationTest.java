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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class IntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void test(){
        FormPerson formPerson = new FormPerson(TestConstants.username, TestConstants.password);
        ResponseEntity<Person> signupResponse = restTemplate.postForEntity("/persons/signup", formPerson, Person.class);
        assertEquals(HttpStatus.OK, signupResponse.getStatusCode());
        assertEquals(TestConstants.username, signupResponse.getBody().getUsername());
        assertNull(signupResponse.getBody().getPassword());

        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/login", formPerson, String.class);
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        String token = loginResponse.getBody();
        Map jsonPayload = TestUtils.decodeJWTPayload(token);
        assertEquals(jsonPayload.get("sub"), TestConstants.username);

        String id = String.valueOf(jsonPayload.get("id"));
        ResponseEntity<Person> getPersonByIdWithoutJWTResponse = restTemplate.exchange("/persons/{id}", HttpMethod.GET, null, Person.class, id);
        assertEquals(HttpStatus.FORBIDDEN, getPersonByIdWithoutJWTResponse.getStatusCode());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Person> getPersonByIdWithJWTResponse = restTemplate.exchange("/persons/{id}", HttpMethod.GET, entity, Person.class, id);
        assertEquals(HttpStatus.OK, getPersonByIdWithJWTResponse.getStatusCode());
        assertEquals(TestConstants.username, getPersonByIdWithJWTResponse.getBody().getUsername());
        assertNull(getPersonByIdWithJWTResponse.getBody().getPassword());
    }
}
