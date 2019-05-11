package com.example.authserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
@AutoConfigureMockMvc(secure = false)
@ActiveProfiles("test")
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
    public void savePerson_CallsServiceSavePersonOnce_WithPassedArgs_ReturnsOKAndPersonWithoutPassword() throws Exception {
        Person newPerson = new Person(TestConstants.username, TestConstants.password);
        when(personService.savePerson(any(Person.class))).thenReturn(newPerson);

        mockMvc.perform(post("/persons/signup")
                .content(TestUtils.asJsonString(new FormPerson(TestConstants.username, TestConstants.password)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(TestConstants.username))
                .andExpect(jsonPath("$.password").doesNotExist());

        verify(personService, times(1)).savePerson(any(Person.class));
    }

    @Test(expected = NestedServletException.class)
    public void findPersonById_WithInvalidId_ThrowsException() throws Exception {
        Long id = 1L;
        doThrow(new IllegalArgumentException()).when(personService).findPersonById(id);

        mockMvc.perform(get("/persons/{id}", id));
    }

    @Test
    public void findPersonById_CallsServiceFindPersonByIdOnce_ReturnsOKAndPersonWithoutPassword() throws Exception {
        Long id = 1L;
        Person newPerson = new Person(TestConstants.username, TestConstants.password);
        when(personService.findPersonById(id)).thenReturn(newPerson);

        mockMvc.perform(get("/persons/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(TestConstants.username))
                .andExpect(jsonPath("$.password").doesNotExist());

        verify(personService, times(1)).findPersonById(id);
    }

}
