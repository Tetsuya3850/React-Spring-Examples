package com.example.todoserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository){
        this.todoRepository = todoRepository;
    }

    public Todo saveTodo(Todo newTodo) {
        return todoRepository.save(newTodo);
    }

    public List<Todo> findAllTodos() {
        return todoRepository.findAll();
    }

    public void deleteTodoById(Long id) {
        todoRepository.deleteById(id);
    }
}
