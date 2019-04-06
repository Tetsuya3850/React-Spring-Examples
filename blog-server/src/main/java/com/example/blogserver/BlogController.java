package com.example.blogserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.security.Principal;

@RestController
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private BlogRepository blogRepository;

    @GetMapping("/")
    List<Blog> all() {
        return blogRepository.findAll();
    }

    @PostMapping("/")
    Blog newEmployee(@Valid @RequestBody Blog newBlog, BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return null;
        }
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(authentication.getName());
        newBlog.setApplicationUser(applicationUser);
        System.out.println(newBlog.getApplicationUser());
        return blogRepository.save(newBlog);
    }

    @GetMapping("/{id}")
    Blog one(@PathVariable Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new BlogNotFoundException(id));
    }

    @GetMapping("/users/{userId}")
    List<Blog> userAll(@PathVariable Long userId, Authentication authentication) {
        ApplicationUser applicationUser = applicationUserRepository.findById(userId)
                .orElseThrow(() -> new ApplicationUserNotFoundException(userId));
        return blogRepository.findByApplicationUser(applicationUser);
    }

    @PutMapping("/{id}")
    Blog replaceBlog(@RequestBody Blog newBlog, @PathVariable Long id) {

        return blogRepository.findById(id)
                .map(blog -> {
                    blog.setTitle(newBlog.getTitle());
                    blog.setText(newBlog.getText());
                    return blogRepository.save(blog);
                })
                .orElseGet(() -> {
                    newBlog.setId(id);
                    return blogRepository.save(newBlog);
                });
    }

    @DeleteMapping("/{id}")
    void deleteTodo(@PathVariable Long id) {
        blogRepository.deleteById(id);
    }

}
