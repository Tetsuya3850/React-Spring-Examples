package com.example.blogserver.article;

import com.example.blogserver.person.Person;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.example.blogserver.commons.TestConstants.*;
import static org.junit.Assert.assertEquals;

@DataJpaTest
@RunWith(SpringRunner.class)
public class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Person person;
    private Person no_article_person;
    private Article article1;
    private Long article1Id;
    private Article article2;

    @Before
    public void setup(){
        person = testEntityManager.persistFlushFind(new Person(USERNAME, PASSWORD));
        no_article_person = testEntityManager.persistFlushFind(new Person("no@gmail.com", PASSWORD));
        article1 = new Article(ARTICLE_TITLE, ARTICLE_TEXT);
        article1.setPerson(person);
        article1 = testEntityManager.persistFlushFind(article1);
        article1Id = article1.getId();
        article2 = new Article(ARTICLE_TITLE, ARTICLE_TEXT);
        article2.setPerson(person);
        article2 = testEntityManager.persistFlushFind(article2);
    }

    @Test
    public void findAllByOrderByCreatedDesc_Success(){
        final int PAGE_SIZE = 1;
        Page<Article> articlesPage = articleRepository.findAllByOrderByCreatedDesc(PageRequest.of(0, PAGE_SIZE));
        assertEquals(1, articlesPage.getContent().size());
        assertEquals(article2, articlesPage.getContent().get(0));
        Page<Article> articlesPage2 = articleRepository.findAllByOrderByCreatedDesc(PageRequest.of(1, PAGE_SIZE));
        assertEquals(1, articlesPage2.getContent().size());
        assertEquals(article1, articlesPage2.getContent().get(0));
    }

    @Test
    public void findByPersonOrderByCreatedDesc_WithNoArticlePerson_ReturnsEmptyList(){
        List<Article> articleList = articleRepository.findByPersonOrderByCreatedDesc(no_article_person);
        assertEquals(0, articleList.size());
    }

    @Test
    public void findByPersonOrderByCreatedDesc_Success(){
        List<Article> articleList = articleRepository.findByPersonOrderByCreatedDesc(person);
        assertEquals(2, articleList.size());
        assertEquals(article2, articleList.get(0));
        assertEquals(article1, articleList.get(1));
    }

    @Test(expected = ArticleNotFoundException.class)
    public void findByIdAndPerson_WithNonOwnerPerson_ThrowsException() {
        articleRepository
                .findByIdAndPerson(article1Id, no_article_person)
                .orElseThrow(() -> new ArticleNotFoundException(article1Id));
    }

    @Test
    public void findByIdAndPerson_Success() {
        Article article = articleRepository
                .findByIdAndPerson(article1Id, person)
                .orElseThrow(() -> new ArticleNotFoundException(article1Id));
        assertEquals(article1, article);
    }

    @Test
    public void deleteByIdAndPerson_WithNonOwnerPerson_DeletesNothing() {
        long result = articleRepository.deleteByIdAndPerson(article1Id, no_article_person);
        assertEquals(0L, result);
    }

    @Test
    public void deleteByIdAndPerson_Success() {
        long result = articleRepository.deleteByIdAndPerson(article1Id, person);
        assertEquals(1L, result);
    }

}
