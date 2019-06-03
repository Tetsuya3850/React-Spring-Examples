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

    private final String username = "me@gmail.com";
    private final String password = "Test3850";
    private final Long id = 1L;

    @Test
    public void savePerson_WithInvalidPerson_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/persons/signup")
                .content(TestUtils.asJsonString(new FormPerson()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void savePerson_CallsServiceSavePersonOnce_WithPassedArgs_ReturnsOKAndPerson_WithoutPassword() throws Exception {
        Person newPerson = new Person(username, password);
        when(personService.savePerson(any(Person.class))).thenReturn(newPerson);

        mockMvc.perform(post("/persons/signup")
                .content(TestUtils.asJsonString(new FormPerson(username, password)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.password").doesNotExist());

        verify(personService, times(1)).savePerson(any(Person.class));
    }

    @Test(expected = NestedServletException.class)
    public void findPersonById_WithInvalidId_ThrowsException() throws Exception {
        when(userDetailsService.loadUserByUsername(username)).thenReturn(new User(username, password, emptyList()));
        doThrow(new IllegalArgumentException()).when(personService).findPersonById(id);

        String token = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .withClaim("id", Long.toString(id))
                .sign(HMAC512(SecurityConstants.JWT_SECRET.getBytes()));

        mockMvc.perform(get("/persons/{id}", id).header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token));
    }

    @Test
    public void findPersonById_CallsServiceFindPersonByIdOnce_ReturnsOKAndPersonWithoutPassword() throws Exception {
        when(userDetailsService.loadUserByUsername(username)).thenReturn(new User(username, password, emptyList()));
        Person newPerson = new Person(username, password);
        when(personService.findPersonById(id)).thenReturn(newPerson);

        String token = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .withClaim("id", Long.toString(id))
                .sign(HMAC512(SecurityConstants.JWT_SECRET.getBytes()));

        mockMvc.perform(get("/persons/{id}", id)
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.password").doesNotExist());

        verify(personService, times(1)).findPersonById(id);
    }

}
