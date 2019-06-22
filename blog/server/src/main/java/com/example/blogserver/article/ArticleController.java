package com.example.blogserver.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("")
    Article saveArticle(@Valid @RequestBody Article newArticle, Authentication auth) {
        return articleService.saveArticle(newArticle, auth);
    }

    @GetMapping("")
    Page<Article> findFeed(@RequestParam(value = "page", defaultValue="0") int pageNum) {
        return articleService.findFeed(pageNum);
    }

    @GetMapping("/persons/{personId}")
    List<Article> findAllPersonArticles(@PathVariable Long personId) {
        return articleService.findAllPersonArticles(personId);
    }

    @GetMapping("/{articleId}")
    Article findArticleById(@PathVariable Long articleId) {
        return articleService.findArticleById(articleId);
    }

    @PutMapping("/{articleId}")
    Article editArticle(@Valid @RequestBody Article editArticle, @PathVariable Long articleId, Authentication auth) {
        return articleService.editArticle(editArticle, articleId, auth);
    }

    @DeleteMapping("/{articleId}")
    void deleteArticle(@PathVariable Long articleId, Authentication auth) {
        articleService.deleteArticle(articleId, auth);
    }
}
