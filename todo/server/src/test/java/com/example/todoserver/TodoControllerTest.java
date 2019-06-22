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

import java.util.ArrayList;
import java.util.List;

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
    public void saveTodo_WithInvalidTodo_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/todos")
                .content(TestUtils.asJsonString(new FormTodo()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void saveTodo_Success() throws Exception {
        Todo newTodo = new Todo(TODO_TEXT);
        when(todoService.saveTodo(any(Todo.class))).thenReturn(newTodo);

        mockMvc.perform(post("/todos")
                .content(TestUtils.asJsonString(new FormTodo(TODO_TEXT)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(TODO_TEXT));

        verify(todoService, times(1)).saveTodo(any(Todo.class));
    }

    @Test
    public void findAllTodos_Success() throws Exception {
        List<Todo> todoList = new ArrayList<>();
        when(todoService.findAllTodos()).thenReturn(todoList);

        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(todoService, times(1)).findAllTodos();
    }

    @Test(expected = NestedServletException.class)
    public void deleteTodoById_WithInvalidId_ThrowsException() throws Exception {
        doThrow(new IllegalArgumentException()).when(todoService).deleteTodoById(TODO_ID);

        mockMvc.perform(delete("/todos/{todoId}", TODO_ID));
    }

    @Test
    public void deleteTodoById_Success() throws Exception {
        doNothing().when(todoService).deleteTodoById(TODO_ID);

        mockMvc.perform(delete("/todos/{todoId}", TODO_ID))
                .andExpect(status().isOk());

        verify(todoService, times(1)).deleteTodoById(TODO_ID);
    }

}
