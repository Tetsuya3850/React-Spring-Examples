package com.example.todoserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {
    @Autowired
    private TodoService todoServie;

    @PostMapping("")
    Todo newTodo(@Valid @RequestBody Todo newTodo) {
        return todoServie.newTodo(newTodo);
    }

    @GetMapping("")
    List<Todo> getTodos() {
        return todoServie.getTodos();
    }

    @DeleteMapping("/{id}")
    void deleteTodo(@PathVariable Long id) {
        todoServie.deleteTodo(id);
    }
}
