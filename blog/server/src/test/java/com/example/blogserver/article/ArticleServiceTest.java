package com.example.blogserver.article;

import com.example.blogserver.person.Person;
import com.example.blogserver.person.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static com.example.blogserver.commons.TestConstants.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private PersonRepository personRepository;

    private ArticleService articleService;

    private Article mockArticle;
    private Person mockPerson;
    private Authentication mockAuth;

    @Before
    public void setup(){
        articleService = new ArticleService(articleRepository, personRepository);
        mockArticle =  new Article(ARTICLE_TITLE, ARTICLE_TEXT);
        mockPerson = new Person(USERNAME, PASSWORD);
        mockAuth = new UsernamePasswordAuthenticationToken(
                mockPerson.getUsername(), mockPerson.getPassword(), new ArrayList<>());
    }

    @Test
    public void saveArticle_Success(){
        class IsArticleWithPerson implements ArgumentMatcher<Article> {
            public boolean matches(Article article) {
                return article.getPerson() != null;
            }
        }

        when(personRepository.findByUsername(mockPerson.getUsername())).thenReturn(Optional.of(mockPerson));
        when(articleRepository.save(argThat(new IsArticleWithPerson()))).thenReturn(mockArticle);

        Article article = articleService.saveArticle(mockArticle, mockAuth);

        assertEquals(mockArticle, article);
        verify(articleRepository, times(1)).save(mockArticle);
    }

    @Test
    public void findFeed_Success(){
        final int pageNum = 0;
        PageRequest pageRequest = PageRequest.of(pageNum, 3);
        Page<Article> mockArticlePage = Page.empty();
        when(articleRepository.findAllByOrderByCreatedDesc(pageRequest)).thenReturn(mockArticlePage);

        Page<Article> articlePage = articleService.findFeed(pageNum);

        assertEquals(mockArticlePage, articlePage);
        verify(articleRepository, times(1)).findAllByOrderByCreatedDesc(pageRequest);
    }

    @Test
    public void findAllPersonArticles_Success(){
        List<Article> mockArticleList = new ArrayList<>();
        when(personRepository.findById(PERSON_ID)).thenReturn(Optional.of(mockPerson));
        when(articleRepository.findByPersonOrderByCreatedDesc(mockPerson)).thenReturn(mockArticleList);

        List<Article> articleList = articleService.findAllPersonArticles(PERSON_ID);

        assertEquals(mockArticleList, articleList);
        verify(articleRepository, times(1)).findByPersonOrderByCreatedDesc(mockPerson);
    }

    @Test(expected = ArticleNotFoundException.class)
    public void findArticleById_WithInvalidId_ThrowsException(){
        doThrow(new ArticleNotFoundException(ARTICLE_ID)).when(articleRepository).findById(ARTICLE_ID);

        articleService.findArticleById(ARTICLE_ID);
    }

    @Test
    public void findArticleById_Success(){
        when(articleRepository.findById(ARTICLE_ID)).thenReturn(Optional.of(mockArticle));

        Article article = articleService.findArticleById(ARTICLE_ID);

        assertEquals(mockArticle, article);
        verify(articleRepository, times(1)).findById(ARTICLE_ID);
    }

    @Test
    public void editArticle_Success(){
        final String EDIT_TITLE = "EDIT_TITLE";
        final String EDIT_TEXT = "EDIT_TEXT";

        class EditedArticle implements ArgumentMatcher<Article> {
            public boolean matches(Article article) {
                return article.getTitle().equals(EDIT_TITLE) && article.getText().equals(EDIT_TEXT);
            }
        }

        Article editArticle = new Article(EDIT_TITLE, EDIT_TEXT);
        when(personRepository.findByUsername(mockPerson.getUsername())).thenReturn(Optional.of(mockPerson));
        when(articleRepository.findByIdAndPerson(ARTICLE_ID, mockPerson)).thenReturn(Optional.of(mockArticle));
        when(articleRepository.save(argThat(new EditedArticle()))).thenReturn(mockArticle);

        Article article = articleService.editArticle(editArticle, ARTICLE_ID, mockAuth);

        assertEquals(mockArticle, article);
        verify(articleRepository, times(1)).save(mockArticle);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteArticle_WithInvalidIdAndPerson_ThrowsException(){
        when(personRepository.findByUsername(mockPerson.getUsername())).thenReturn(Optional.of(mockPerson));
        when(articleRepository.deleteByIdAndPerson(ARTICLE_ID, mockPerson)).thenReturn(0L);

        articleService.deleteArticle(ARTICLE_ID, mockAuth);
    }

    @Test
    public void deleteArticle_Success(){
        when(personRepository.findByUsername(mockPerson.getUsername())).thenReturn(Optional.of(mockPerson));
        when(articleRepository.deleteByIdAndPerson(ARTICLE_ID, mockPerson)).thenReturn(1L);

        articleService.deleteArticle(ARTICLE_ID, mockAuth);

        verify(articleRepository, times(1)).deleteByIdAndPerson(ARTICLE_ID, mockPerson);
    }

}
