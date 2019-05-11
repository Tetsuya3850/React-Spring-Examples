package com.example.todoserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService){
        this.todoService = todoService;
    }

    @PostMapping("")
    public Todo saveTodo(@Valid @RequestBody Todo todo) {
        return todoService.saveTodo(todo);
    }

    @GetMapping("")
    public List<Todo> findAllTodos() {
        return todoService.findAllTodos();
    }

    @DeleteMapping("/{id}")
    public void deleteTodoById(@PathVariable Long id) {
        todoService.deleteTodoById(id);
    }
}
