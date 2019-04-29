package com.example.todoserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;

    Todo newTodo(Todo newTodo) {
        return todoRepository.save(newTodo);
    }

    List<Todo> getTodos() {
        return todoRepository.findAll();
    }

    void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }
}
