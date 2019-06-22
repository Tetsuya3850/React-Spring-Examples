package com.example.twitterserver.person;

import com.example.twitterserver.follow.FollowRepository;
import com.example.twitterserver.heart.HeartRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.example.twitterserver.commons.TestConstants.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private HeartRepository heartRepository;

    @Mock
    private FollowRepository followRepository;

    private PersonService personService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private Person mockPerson;
    private Authentication mockAuth;

    @Before
    public void setup(){
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        personService = new PersonService(personRepository, heartRepository, followRepository, bCryptPasswordEncoder);
        mockPerson = new Person(USERNAME, PASSWORD);
        mockAuth = new UsernamePasswordAuthenticationToken(
                mockPerson.getUsername(), mockPerson.getPassword(), new ArrayList<>());
    }

    @Test
    public void savePerson_Success(){
        class IsPersonWithBCryptEncodedPassword implements ArgumentMatcher<Person> {
            private Pattern BCRYPT_PATTERN = Pattern
                    .compile("\\A\\$2(a|y|b)?\\$\\d\\d\\$[./0-9A-Za-z]{53}");

            public boolean matches(Person person) {
                return BCRYPT_PATTERN.matcher(person.getPassword()).matches();
            }
        }

        when(personRepository.save(argThat(new IsPersonWithBCryptEncodedPassword()))).thenReturn(mockPerson);

        Person person = personService.savePerson(mockPerson);

        assertEquals(USERNAME, person.getUsername());
        verify(personRepository, times(1)).save(mockPerson);
    }

    @Test
    public void findAllPersons_Success(){
        List<Person> mockPersonList = new ArrayList<>();
        when(personRepository.findAll()).thenReturn(mockPersonList);

        List<Person> personList = personService.findAllPersons();

        assertEquals(mockPersonList, personList);
        verify(personRepository, times(1)).findAll();
    }

    @Test(expected = PersonNotFoundException.class)
    public void findPersonById_WithInvalidId_ThrowsException(){
        doThrow(new PersonNotFoundException(PERSON_ID)).when(personRepository).findById(PERSON_ID);

        personService.findPersonById(PERSON_ID);
    }

    @Test
    public void findPersonById_Success(){
        when(personRepository.findById(PERSON_ID)).thenReturn(Optional.of(mockPerson));

        Person person = personService.findPersonById(PERSON_ID);

        assertEquals(mockPerson, person);
        verify(personRepository, times(1)).findById(PERSON_ID);
    }

    @Test
    public void getOwnInfo_Success() throws Exception {
        when(personRepository.findByUsername(mockPerson.getUsername())).thenReturn(Optional.of(mockPerson));
        List<Long> mockHeartedTweetIds = new ArrayList<>();
        List<Long> mockFollowingPersonIds = new ArrayList<>();
        when(heartRepository.getAllHeartedTweetIdsByPerson(mockPerson)).thenReturn(mockHeartedTweetIds);
        when(followRepository.getAllFollowingPersonIdsByPerson(mockPerson)).thenReturn(mockFollowingPersonIds);

        OwnInfo ownInfo = personService.getOwnInfo(mockAuth);

        assertEquals(ownInfo.getHeartedTweetIds(), mockHeartedTweetIds);
        assertEquals(ownInfo.getFollowingPersonIds(), mockFollowingPersonIds);
        verify(personRepository, times(1)).findByUsername(mockPerson.getUsername());
        verify(heartRepository, times(1)).getAllHeartedTweetIdsByPerson(mockPerson);
        verify(followRepository, times(1)).getAllFollowingPersonIdsByPerson(mockPerson);
    }
}
