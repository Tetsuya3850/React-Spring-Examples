package com.example.todoserver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    private TodoService todoService;

    private String text = "run";

    @Before
    public void setup(){
        todoService = new TodoService(todoRepository);
    }

    @Test
    public void saveTodo_CallsRepositorySaveOnce_WithPassedArgs_ReturnsTodo(){
        Todo mockTodo = new Todo(text);
        when(todoRepository.save(mockTodo)).thenReturn(mockTodo);

        Todo todo = todoService.saveTodo(mockTodo);

        assertEquals(mockTodo, todo);
        verify(todoRepository, times(1)).save(mockTodo);
    }

    @Test
    public void findAllTodos_CallsRepositoryFindAllOnce_ReturnsTodos(){
        Todo mockTodo = new Todo(text);
        List<Todo> mockTodos = Arrays.asList(mockTodo);
        when(todoRepository.findAll()).thenReturn(mockTodos);

        List<Todo> todos = todoService.findAllTodos();

        assertEquals(mockTodo, todos.get(0));
        verify(todoRepository, times(1)).findAll();
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteTodoById_WithInvalidId_ThrowsException(){
        Long id = 1L;
        doThrow(new IllegalArgumentException()).when(todoRepository).deleteById(id);

        todoService.deleteTodoById(id);
    }

    @Test
    public void deleteTodoById_CallsRepositoryDeleteByIdOnce_WithPassedArgs(){
        Long id = 1L;
        doNothing().when(todoRepository).deleteById(id);

        todoService.deleteTodoById(id);

        verify(todoRepository, times(1)).deleteById(id);
    }

}
