package com.example.blogserver.article;

import com.example.blogserver.user.ApplicationUser;
import com.example.blogserver.user.ApplicationUserNotFoundException;
import com.example.blogserver.user.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
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

    int page_size = 3;

    @GetMapping("")
    Page<Article> getFeed(@RequestParam(value = "page", defaultValue="0") int pageNum) {
        return articleRepository.findAllByOrderByCreatedDesc(PageRequest.of(pageNum, page_size));
    }

    @PostMapping("")
    Article postArticle(@Valid @RequestBody Article newArticle, Authentication authentication) {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(authentication.getName());
        newArticle.setApplicationUser(applicationUser);
        return articleRepository.save(newArticle);
    }

    @GetMapping("/{articleId}")
    Article getArticle(@PathVariable Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException(articleId));
    }

    @GetMapping("/users/{userId}")
    List<Article> getAllUserArticles(@PathVariable Long userId) {
        ApplicationUser applicationUser = applicationUserRepository.findById(userId)
                .orElseThrow(() -> new ApplicationUserNotFoundException(userId));
        return articleRepository.findByApplicationUserOrderByCreatedDesc(applicationUser);
    }

    @PutMapping("/{articleId}")
    Article editArticle(@RequestBody Article newArticle, @PathVariable Long articleId, Authentication auth) {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(auth.getName());
        Article article = articleRepository.findByIdAndApplicationUser(articleId, applicationUser).orElseThrow(() -> new ArticleNotFoundException(articleId));
        article.setTitle(newArticle.getTitle());
        article.setText(newArticle.getText());
        return articleRepository.save(article);
    }

    @DeleteMapping("/{articleId}")
    void deleteArticle(@PathVariable Long articleId, Authentication auth) {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(auth.getName());
        Article article = articleRepository.findByIdAndApplicationUser(articleId, applicationUser).orElseThrow(() -> new ArticleNotFoundException(articleId));
        articleRepository.delete(article);
    }
}
