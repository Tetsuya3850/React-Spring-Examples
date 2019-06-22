package com.example.blogserver.article;

import com.example.blogserver.person.Person;
import com.example.blogserver.person.PersonNotFoundException;
import com.example.blogserver.person.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final PersonRepository personRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository,
                          PersonRepository personRepository){
        this.articleRepository = articleRepository;
        this.personRepository = personRepository;
    }

    private final int PAGE_SIZE = 3;

    Article saveArticle(Article newArticle, Authentication auth) {
        String username = auth.getName();
        Person person = personRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        newArticle.setPerson(person);
        return articleRepository.save(newArticle);
    }

    Page<Article> findFeed(int pageNum) {
        return articleRepository.findAllByOrderByCreatedDesc(PageRequest.of(pageNum, PAGE_SIZE));
    }

    List<Article> findAllPersonArticles(Long personId) {
        Person person = personRepository
                .findById(personId)
                .orElseThrow(() -> new PersonNotFoundException(personId));
        return articleRepository.findByPersonOrderByCreatedDesc(person);
    }

    Article findArticleById(Long articleId) {
        return articleRepository
                .findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException(articleId));
    }

    Article editArticle(Article editArticle, Long articleId, Authentication auth) {
        String username = auth.getName();
        Person person = personRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        Article article = articleRepository
                .findByIdAndPerson(articleId, person)
                .orElseThrow(() -> new ArticleNotFoundException(articleId));
        article.setTitle(editArticle.getTitle());
        article.setText(editArticle.getText());
        return articleRepository.save(article);
    }

    @Transactional
    public void deleteArticle(Long articleId, Authentication auth) {
        String username = auth.getName();
        Person person = personRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        long result = articleRepository.deleteByIdAndPerson(articleId, person);
        if(result == 0L){
            throw new IllegalArgumentException();
        }
    }
}
