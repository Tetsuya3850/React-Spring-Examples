package com.example.twitterserver.follow;

import com.example.twitterserver.person.Person;
import com.example.twitterserver.person.PersonRepository;
import com.example.twitterserver.tweet.Tweet;
import com.example.twitterserver.tweet.TweetRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.twitterserver.commons.TestConstants.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FollowServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private FollowRepository followRepository;

    private FollowService followService;

    private Person mockPerson;
    private Person anotherMockPerson;
    private Authentication mockAuth;
    private Follow mockFollow;

    @Before
    public void setup(){
        followService = new FollowService(followRepository, personRepository);
        mockPerson = new Person(USERNAME, PASSWORD);
        mockAuth = new UsernamePasswordAuthenticationToken(
                mockPerson.getUsername(), mockPerson.getPassword(), new ArrayList<>());
        anotherMockPerson = new Person(ANOTHER_USERNAME, PASSWORD);
        mockFollow = new Follow(mockPerson, anotherMockPerson);
    }

    @Test
    public void toggleFollow_saveFollow(){
        when(personRepository.findByUsername(USERNAME)).thenReturn(Optional.of(mockPerson));
        when(personRepository.findById(PERSON_ID)).thenReturn(Optional.of(anotherMockPerson));
        when(followRepository.findByFollowerAndFollowee(mockPerson, anotherMockPerson)).thenReturn(null);

        followService.toggleFollow(PERSON_ID, mockAuth);

        verify(followRepository, times(1)).save(any(Follow.class));
        verify(personRepository, times(1)).save(mockPerson);
        verify(personRepository, times(1)).save(anotherMockPerson);
    }

    @Test
    public void  toggleFollow_deleteFollow(){
        when(personRepository.findByUsername(USERNAME)).thenReturn(Optional.of(mockPerson));
        when(personRepository.findById(PERSON_ID)).thenReturn(Optional.of(anotherMockPerson));
        when(followRepository.findByFollowerAndFollowee(mockPerson, anotherMockPerson)).thenReturn(mockFollow);

        followService.toggleFollow(PERSON_ID, mockAuth);

        verify(followRepository, times(1)).delete(mockFollow);
        verify(personRepository, times(1)).save(mockPerson);
        verify(personRepository, times(1)).save(anotherMockPerson);
    }

    @Test
    public void getAllFollowingByPersonId_Success(){
        List<Person> mockPersonList = new ArrayList<>();
        when(followRepository.getAllFollowingByPersonId(PERSON_ID)).thenReturn(mockPersonList);

        List<Person> personList = followService.getAllFollowingByPersonId(PERSON_ID);

        assertEquals(mockPersonList, personList);
        verify(followRepository, times(1)).getAllFollowingByPersonId(PERSON_ID);
    }

    @Test
    public void getAllFollowersByPersonId_Success(){
        List<Person> mockPersonList = new ArrayList<>();
        when(followRepository.getAllFollowersByPersonId(PERSON_ID)).thenReturn(mockPersonList);

        List<Person> personList = followService.getAllFollowersByPersonId(PERSON_ID);

        assertEquals(mockPersonList, personList);
        verify(followRepository, times(1)).getAllFollowersByPersonId(PERSON_ID);
    }

}
