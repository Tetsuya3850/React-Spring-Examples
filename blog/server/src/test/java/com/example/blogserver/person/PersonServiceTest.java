package com.example.blogserver.person;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.regex.Pattern;

import static com.example.blogserver.commons.TestConstants.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    private PersonService personService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private Person mockPerson;

    @Before
    public void setup(){
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        personService = new PersonService(personRepository, bCryptPasswordEncoder);
        mockPerson = new Person(USERNAME, PASSWORD);
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
}
