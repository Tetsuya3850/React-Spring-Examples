package com.example.authserver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    private PersonService personService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    class IsPersonWithBCryptEncodedPassword implements ArgumentMatcher<Person> {
        private Pattern BCRYPT_PATTERN = Pattern
                .compile("\\A\\$2(a|y|b)?\\$\\d\\d\\$[./0-9A-Za-z]{53}");

        public boolean matches(Person person) {
            return BCRYPT_PATTERN.matcher(person.getPassword()).matches();
        }
    }


    @Before
    public void setup(){
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        personService = new PersonService(personRepository, bCryptPasswordEncoder);
    }

    @Test
    public void savePerson_EncodesPassword_CallsRepositorySaveOnce_WithPassedArgs_ReturnsPerson(){
        Person mockPerson = new Person(TestConstants.username, TestConstants.password);
        Person passwordEncodedMockPerson = new Person(TestConstants.username, TestConstants.password);
        passwordEncodedMockPerson.setPassword(bCryptPasswordEncoder.encode(passwordEncodedMockPerson.getPassword()));
        when(personRepository.save(argThat(new IsPersonWithBCryptEncodedPassword()))).thenReturn(passwordEncodedMockPerson);

        Person person = personService.savePerson(mockPerson);

        assertEquals(passwordEncodedMockPerson, person);
        verify(personRepository, times(1)).save(mockPerson);
    }

    @Test(expected = PersonNotFoundException.class)
    public void findPersonById_WithInvalidId_ThrowsException(){
        doThrow(new PersonNotFoundException(TestConstants.id)).when(personRepository).findById(TestConstants.id);

        personService.findPersonById(TestConstants.id);
    }

    @Test
    public void findPersonById_CallsRepositoryFindByIdOnce_WithPassedArgs_ReturnsPerson(){
        Person mockPerson = new Person(TestConstants.username, TestConstants.password);
        when(personRepository.findById(TestConstants.id)).thenReturn(Optional.of(mockPerson));

        Person person = personService.findPersonById(TestConstants.id);

        assertEquals(mockPerson, person);
        verify(personRepository, times(1)).findById(TestConstants.id);
    }

}
