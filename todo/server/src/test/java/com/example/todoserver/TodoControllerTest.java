package com.example.todoserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;
import java.util.Arrays;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static com.example.todoserver.TestConstants.*;

@RunWith(SpringRunner.class)
@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @Test
    public void saveTodo_WithInvalidText_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/todos")
                .content(TestUtils.asJsonString(new FormTodo()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void saveTodo_CallsServiceSaveTodoOnce_WithPassedArgs_ReturnsOKAndTodo() throws Exception {
        Todo newTodo = new Todo(TEXT);
        when(todoService.saveTodo(any(Todo.class))).thenReturn(newTodo);

        mockMvc.perform(post("/todos")
                .content(TestUtils.asJsonString(new FormTodo(TEXT)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(TEXT));

        verify(todoService, times(1)).saveTodo(any(Todo.class));
    }

    @Test
    public void findAllTodos_CallsServiceFindAllTodosOnce_ReturnsOKAndTodos() throws Exception {
        when(todoService.findAllTodos()).thenReturn(Arrays.asList(new Todo(TEXT)));

        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text").value(TEXT));

        verify(todoService, times(1)).findAllTodos();
    }

    @Test(expected = NestedServletException.class)
    public void deleteTodoById_WithInvalidId_ThrowsException() throws Exception {
        Long id = 1L;
        doThrow(new IllegalArgumentException()).when(todoService).deleteTodoById(id);

        mockMvc.perform(delete("/todos/{id}", id));
    }

    @Test
    public void deleteTodoById_CallsServiceDeleteTodoByIdOnce_WithPassedArgs_ReturnsOK() throws Exception {
        Long id = 1L;
        doNothing().when(todoService).deleteTodoById(id);

        mockMvc.perform(delete("/todos/{id}", id))
                .andExpect(status().isOk());

        verify(todoService, times(1)).deleteTodoById(id);
    }

}
