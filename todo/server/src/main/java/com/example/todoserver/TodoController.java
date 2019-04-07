package com.example.todoserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {
    @Autowired
    private TodoRepository todoRepository;

    @PostMapping("")
    Todo newTodo(@Valid @RequestBody Todo newTodo) {
        return todoRepository.save(newTodo);
    }

    @GetMapping("")
    List<Todo> getTodos() {
        return todoRepository.findAll();
    }

    @DeleteMapping("/{id}")
    void deleteTodo(@PathVariable Long id) {
        todoRepository.deleteById(id);
    }
}
