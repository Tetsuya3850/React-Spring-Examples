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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class IntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void test(){
        String text = "run";
        FormTodo formTodo = new FormTodo(text);
        ResponseEntity<Todo> postTodoResponse = restTemplate.postForEntity("/todos", formTodo, Todo.class);

        assertEquals(HttpStatus.OK, postTodoResponse.getStatusCode());
        assertEquals(text, postTodoResponse.getBody().getText());

        ResponseEntity<Todo[]> getTodosResponse = restTemplate.getForEntity("/todos", Todo[].class);

        assertEquals(HttpStatus.OK, getTodosResponse.getStatusCode());
        assertEquals(1, getTodosResponse.getBody().length);

        Long todo_id = postTodoResponse.getBody().getId();
        restTemplate.delete("/todos/{id}", todo_id);

        ResponseEntity<Todo[]> getTodosAfterDeleteResponse = restTemplate.getForEntity("/todos", Todo[].class);
        assertEquals(0, getTodosAfterDeleteResponse.getBody().length);
    }
}
