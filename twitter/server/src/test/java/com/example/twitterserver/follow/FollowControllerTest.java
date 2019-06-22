package com.example.twitterserver.follow;

import com.auth0.jwt.JWT;
import com.example.twitterserver.person.Person;
import com.example.twitterserver.person.SecurityConstants;
import com.example.twitterserver.person.UserDetailsServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.example.twitterserver.commons.TestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FollowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FollowService followService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private String token;

    @Before
    public void setup(){
        token = JWT.create()
                .withSubject(USERNAME)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .withClaim("id", Long.toString(PERSON_ID))
                .sign(HMAC512(SecurityConstants.JWT_SECRET.getBytes()));
        when(userDetailsService.loadUserByUsername(USERNAME)).thenReturn(new User(USERNAME, PASSWORD, new ArrayList()));
    }

    @Test
    public void toggleFollow_Success() throws Exception {
        doNothing().when(followService).toggleFollow(eq(PERSON_ID), any(UsernamePasswordAuthenticationToken.class));

        mockMvc.perform(post("/follows/{followeeId}", PERSON_ID)
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token))
                .andExpect(status().isOk());

        verify(followService, times(1)).toggleFollow(eq(PERSON_ID), any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void getAllFollowingByPersonId_Success() throws Exception {
        List<Person> mockPersonList = new ArrayList<>();
        when(followService.getAllFollowingByPersonId(PERSON_ID)).thenReturn(mockPersonList);

        mockMvc.perform(get("/follows/following/{personId}", PERSON_ID)
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(followService, times(1)).getAllFollowingByPersonId(PERSON_ID);
    }

    @Test
    public void getAllFollowersByPersonId_Success() throws Exception {
        List<Person> mockPersonList = new ArrayList<>();
        when(followService.getAllFollowersByPersonId(PERSON_ID)).thenReturn(mockPersonList);

        mockMvc.perform(get("/follows/followers/{personId}", PERSON_ID)
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(followService, times(1)).getAllFollowersByPersonId(PERSON_ID);
    }

}
