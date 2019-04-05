package com.example.todoserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class TodoController {
    @Autowired
    private TodoRepository todoRepository;

    @GetMapping("/todos")
    List<Todo> all() {
        return todoRepository.findAll();
    }

    @PostMapping("/todos")
    Todo newEmployee(@Valid @RequestBody Todo newTodo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return null;
        }

        return todoRepository.save(newTodo);
    }

    @GetMapping("/todos/{id}")
    Todo one(@PathVariable Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
    }

    @PutMapping("/todos/{id}")
    Todo replaceEmployee(@RequestBody Todo newTodo, @PathVariable Long id) {

        return todoRepository.findById(id)
                .map(employee -> {
                    employee.setText(newTodo.getText());
                    return todoRepository.save(employee);
                })
                .orElseGet(() -> {
                    newTodo.setId(id);
                    return todoRepository.save(newTodo);
                });
    }

    @DeleteMapping("/todos/{id}")
    void deleteTodo(@PathVariable Long id) {
        todoRepository.deleteById(id);
    }
}
