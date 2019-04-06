package com.example.todoserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {
    @Autowired
    private TodoRepository todoRepository;

    @GetMapping("/")
    List<Todo> all() {
        return todoRepository.findAll();
    }

    @PostMapping("/")
    Todo newEmployee(@Valid @RequestBody Todo newTodo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return null;
        }

        return todoRepository.save(newTodo);
    }

    @GetMapping("/{id}")
    Todo one(@PathVariable Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
    }

    @PutMapping("/{id}")
    Todo replaceEmployee(@RequestBody Todo newTodo, @PathVariable Long id) {

        return todoRepository.findById(id)
                .map(todo -> {
                    todo.setText(newTodo.getText());
                    return todoRepository.save(todo);
                })
                .orElseGet(() -> {
                    newTodo.setId(id);
                    return todoRepository.save(newTodo);
                });
    }

    @DeleteMapping("/{id}")
    void deleteTodo(@PathVariable Long id) {
        todoRepository.deleteById(id);
    }
}
