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
    Todo saveTodo(@Valid @RequestBody Todo newTodo) {
        return todoService.saveTodo(newTodo);
    }

    @GetMapping("")
    List<Todo> findAllTodos() {
        return todoService.findAllTodos();
    }

    @DeleteMapping("/{todoId}")
    void deleteTodoById(@PathVariable Long todoId) {
        todoService.deleteTodoById(todoId);
    }
}
