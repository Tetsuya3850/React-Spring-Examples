package com.example.todoserver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static com.example.todoserver.TestConstants.*;

@RunWith(MockitoJUnitRunner.class)
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    private TodoService todoService;

    @Before
    public void setup(){
        todoService = new TodoService(todoRepository);
    }

    @Test
    public void saveTodo_Success(){
        Todo mockTodo = new Todo(TODO_TEXT);
        when(todoRepository.save(mockTodo)).thenReturn(mockTodo);

        Todo todo = todoService.saveTodo(mockTodo);

        assertEquals(mockTodo, todo);
        verify(todoRepository, times(1)).save(mockTodo);
    }

    @Test
    public void findAllTodos_Success(){
        List<Todo> mockTodoList = new ArrayList<>();
        when(todoRepository.findAll()).thenReturn(mockTodoList);

        List<Todo> todoList = todoService.findAllTodos();

        assertEquals(mockTodoList, todoList);
        verify(todoRepository, times(1)).findAll();
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteTodoById_WithInvalidId_ThrowsException(){
        doThrow(new IllegalArgumentException()).when(todoRepository).deleteById(TODO_ID);

        todoService.deleteTodoById(TODO_ID);
    }

    @Test
    public void deleteTodoById_Success(){
        doNothing().when(todoRepository).deleteById(TODO_ID);

        todoService.deleteTodoById(TODO_ID);

        verify(todoRepository, times(1)).deleteById(TODO_ID);
    }

}
