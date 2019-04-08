package com.example.blogserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("")
    Page<Article> all(@RequestParam(value = "page", defaultValue="0") int pageNum) {
        return articleRepository.findAll(PageRequest.of(pageNum, 5));
    }

    @PostMapping("")
    Article newEmployee(@Valid @RequestBody Article newArticle, Authentication authentication) {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(authentication.getName());
        newArticle.setApplicationUser(applicationUser);
        System.out.println(newArticle.getApplicationUser());
        return articleRepository.save(newArticle);
    }

    @GetMapping("/{id}")
    Article one(@PathVariable Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException(id));
    }

    @GetMapping("/users/{userId}")
    List<Article> userAll(@PathVariable Long userId) {
        ApplicationUser applicationUser = applicationUserRepository.findById(userId)
                .orElseThrow(() -> new ApplicationUserNotFoundException(userId));
        return articleRepository.findByApplicationUser(applicationUser);
    }

    @PutMapping("/{id}")
    Article replaceBlog(@RequestBody Article newArticle, @PathVariable Long id, Authentication authentication) {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(authentication.getName());
        Article article = articleRepository.findByIdAndApplicationUser(id, applicationUser);
        article.setTitle(newArticle.getTitle());
        article.setText(newArticle.getText());
        return articleRepository.save(article);
    }

    @DeleteMapping("/{id}")
    void deleteTodo(@PathVariable Long id, Authentication authentication) {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(authentication.getName());
        Article article = articleRepository.findByIdAndApplicationUser(id, applicationUser);
        articleRepository.delete(article);
    }
}
