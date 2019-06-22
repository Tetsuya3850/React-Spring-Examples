package com.example.todoserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static com.example.todoserver.TestConstants.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class IntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void test() {
        // saveTodo Success
        FormTodo formTodo = new FormTodo(TODO_TEXT);
        ResponseEntity<Todo> postTodoResponse = restTemplate.postForEntity("/todos", formTodo, Todo.class);
        assertEquals(HttpStatus.OK, postTodoResponse.getStatusCode());
        assertEquals(TODO_TEXT, postTodoResponse.getBody().getText());

        // findAllTodos Success
        ResponseEntity<Todo[]> getTodosResponse = restTemplate.getForEntity("/todos", Todo[].class);
        assertEquals(HttpStatus.OK, getTodosResponse.getStatusCode());
        assertEquals(1, getTodosResponse.getBody().length);

        // deleteTodoById Success
        Long todo1Id = postTodoResponse.getBody().getId();
        restTemplate.delete("/todos/{todoId}", todo1Id);
        ResponseEntity<Todo[]> getTodosAfterDeleteResponse = restTemplate.getForEntity("/todos", Todo[].class);
        assertEquals(0, getTodosAfterDeleteResponse.getBody().length);
    }
}
