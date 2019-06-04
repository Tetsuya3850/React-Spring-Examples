package com.example.authserver;

import com.auth0.jwt.JWT;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.example.authserver.TestConstants.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void savePerson_WithInvalidPerson_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/persons/signup")
                .content(TestUtils.asJsonString(new FormPerson()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void savePerson_CallsServiceSavePersonOnce_WithPassedArgs_ReturnsOKAndPerson_WithoutPassword() throws Exception {
        Person newPerson = new Person(USERNAME, PASSWORD);
        when(personService.savePerson(any(Person.class))).thenReturn(newPerson);

        mockMvc.perform(post("/persons/signup")
                .content(TestUtils.asJsonString(new FormPerson(USERNAME, PASSWORD)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.password").doesNotExist());

        verify(personService, times(1)).savePerson(any(Person.class));
    }

    @Test(expected = NestedServletException.class)
    public void findPersonById_WithInvalidId_ThrowsException() throws Exception {
        when(userDetailsService.loadUserByUsername(USERNAME)).thenReturn(new User(USERNAME, PASSWORD, emptyList()));
        doThrow(new IllegalArgumentException()).when(personService).findPersonById(PERSON_ID);

        String token = JWT.create()
                .withSubject(USERNAME)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .withClaim("id", Long.toString(PERSON_ID))
                .sign(HMAC512(SecurityConstants.JWT_SECRET.getBytes()));

        mockMvc.perform(get("/persons/{personId}", PERSON_ID).header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token));
    }

    @Test
    public void findPersonById_CallsServiceFindPersonByIdOnce_ReturnsOKAndPersonWithoutPassword() throws Exception {
        when(userDetailsService.loadUserByUsername(USERNAME)).thenReturn(new User(USERNAME, PASSWORD, emptyList()));
        Person newPerson = new Person(USERNAME, PASSWORD);
        when(personService.findPersonById(PERSON_ID)).thenReturn(newPerson);

        String token = JWT.create()
                .withSubject(USERNAME)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .withClaim("id", Long.toString(PERSON_ID))
                .sign(HMAC512(SecurityConstants.JWT_SECRET.getBytes()));

        mockMvc.perform(get("/persons/{personId}", PERSON_ID)
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.password").doesNotExist());

        verify(personService, times(1)).findPersonById(PERSON_ID);
    }

}
