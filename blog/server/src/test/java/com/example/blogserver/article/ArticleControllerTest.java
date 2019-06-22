package com.example.blogserver.article;

import com.auth0.jwt.JWT;
import com.example.blogserver.commons.TestUtils;
import com.example.blogserver.person.SecurityConstants;
import com.example.blogserver.person.UserDetailsServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.example.blogserver.commons.TestConstants.*;
import static java.util.Collections.emptyList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private String token;
    private Article mockArticle;

    @Before
    public void setup(){
        token = JWT.create()
                .withSubject(USERNAME)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .withClaim("id", Long.toString(PERSON_ID))
                .sign(HMAC512(SecurityConstants.JWT_SECRET.getBytes()));
        mockArticle =  new Article(ARTICLE_TITLE, ARTICLE_TEXT);
        when(userDetailsService.loadUserByUsername(USERNAME)).thenReturn(new User(USERNAME, PASSWORD, emptyList()));
    }

    @Test
    public void saveArticle_WithInvalidArticle_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/articles")
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token)
                .content(TestUtils.asJsonString(new FormArticle()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void saveArticle_Success() throws Exception {
        when(articleService.saveArticle(any(Article.class), any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockArticle);

        mockMvc.perform(post("/articles")
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token)
                .content(TestUtils.asJsonString(new FormArticle(ARTICLE_TITLE, ARTICLE_TEXT)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(ARTICLE_TITLE))
                .andExpect(jsonPath("$.text").value(ARTICLE_TEXT));

        verify(articleService, times(1)).saveArticle(any(Article.class), any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void findFeed_WithoutSpecifiedPage_CallsServiceFindFeedWithDefaultValue() throws Exception {
        final int DEFAULT_VALUE = 0;
        Page<Article> mockArticlePage = Page.empty();
        when(articleService.findFeed(DEFAULT_VALUE)).thenReturn(mockArticlePage);

        mockMvc.perform(get("/articles")
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));

        verify(articleService, times(1)).findFeed(DEFAULT_VALUE);
    }

    @Test
    public void findFeed_Success() throws Exception {
        final int PAGE_NUM = 1;
        Page<Article> mockArticlePage = Page.empty();
        when(articleService.findFeed(PAGE_NUM)).thenReturn(mockArticlePage);

        mockMvc.perform(get("/articles/?page={PAGE_NUM}", PAGE_NUM)
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));

        verify(articleService, times(1)).findFeed(PAGE_NUM);
    }

    @Test
    public void findAllPersonArticles_Success() throws Exception {
        List<Article> articleList = new ArrayList<>();
        when(articleService.findAllPersonArticles(PERSON_ID)).thenReturn(articleList);

        mockMvc.perform(get("/articles/persons/{personId}", PERSON_ID)
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(articleService, times(1)).findAllPersonArticles(PERSON_ID);
    }

    @Test(expected = NestedServletException.class)
    public void findArticleById_WithInvalidId_ThrowsException() throws Exception {
        doThrow(new IllegalArgumentException()).when(articleService).findArticleById(ARTICLE_ID);

        mockMvc.perform(get("/articles/{articleId}", ARTICLE_ID)
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token));
    }

    @Test
    public void findArticleById_Success() throws Exception {
        when(articleService.findArticleById(ARTICLE_ID)).thenReturn(mockArticle);

        mockMvc.perform(get("/articles/{articleId}", ARTICLE_ID)
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(ARTICLE_TITLE))
                .andExpect(jsonPath("$.text").value(ARTICLE_TEXT));

        verify(articleService, times(1)).findArticleById(ARTICLE_ID);
    }

    @Test
    public void editArticle_WithInvalidArticle_ReturnsBadRequest() throws Exception {
        mockMvc.perform(put("/articles/{articleId}", ARTICLE_ID)
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token)
                .content(TestUtils.asJsonString(new FormArticle()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editArticle_Success() throws Exception {
        when(articleService.editArticle(any(Article.class), eq(ARTICLE_ID), any(
                UsernamePasswordAuthenticationToken.class))).thenReturn(mockArticle);

        mockMvc.perform(put("/articles/{articleId}", ARTICLE_ID)
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token)
                .content(TestUtils.asJsonString(new FormArticle(ARTICLE_TITLE, ARTICLE_TEXT)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(ARTICLE_TITLE))
                .andExpect(jsonPath("$.text").value(ARTICLE_TEXT));

        verify(articleService, times(1)).editArticle(
                any(Article.class), eq(ARTICLE_ID), any(UsernamePasswordAuthenticationToken.class));
    }

    @Test(expected = NestedServletException.class)
    public void deleteArticle_WithInvalidIdAndPerson_ThrowsException() throws Exception {
        doThrow(new IllegalArgumentException()).when(articleService).deleteArticle(
                eq(ARTICLE_ID), any(UsernamePasswordAuthenticationToken.class));

        mockMvc.perform(delete("/articles/{articleId}", ARTICLE_ID)
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token));
    }

    @Test
    public void deleteArticle_Success() throws Exception {
        doNothing().when(articleService).deleteArticle(eq(ARTICLE_ID), any(UsernamePasswordAuthenticationToken.class));

        mockMvc.perform(delete("/articles/{articleId}", ARTICLE_ID)
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token))
                .andExpect(status().isOk());

        verify(articleService, times(1)).deleteArticle(
                eq(ARTICLE_ID), any(UsernamePasswordAuthenticationToken.class));
    }

}
