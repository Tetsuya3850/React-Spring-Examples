package com.example.instagramserver.post;

import com.auth0.jwt.JWT;
import com.example.instagramserver.commons.TestUtils;
import com.example.instagramserver.person.SecurityConstants;
import com.example.instagramserver.person.UserDetailsServiceImpl;
import com.example.instagramserver.tag.Tag;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.example.instagramserver.commons.TestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private String token;
    private Post mockPost;
    private Tag mockTag1;
    private Tag mockTag2;

    @Before
    public void setup(){
        token = JWT.create()
                .withSubject(USERNAME)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .withClaim("id", PERSON_ID)
                .sign(HMAC512(SecurityConstants.JWT_SECRET.getBytes()));
        mockPost = new Post(POST_ID, POST_IMG_PATH , POST_DESCRIPTION);
        mockTag1 = new Tag(TAG_TEXT_1);
        mockTag2 = new Tag(TAG_TEXT_2);
        mockPost.addTag(mockTag1);
        mockPost.addTag(mockTag2);
        when(userDetailsService.loadUserByUsername(USERNAME)).thenReturn(new User(USERNAME, PASSWORD, new ArrayList()));
    }

    @Test
    public void savePost_Success() throws Exception {
        when(postService.savePost(any(Post.class), any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockPost);

        mockMvc.perform(post("/posts")
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token)
                .content(TestUtils.asJsonString(new FormPost(POST_ID, POST_IMG_PATH, POST_DESCRIPTION, Arrays.asList(TAG_TEXT_1, TAG_TEXT_2))))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(POST_DESCRIPTION))
                .andExpect(jsonPath("$.tags.[0].text").value(TAG_TEXT_1))
                .andExpect(jsonPath("$.tags.[1].text").value(TAG_TEXT_2));

        verify(postService, times(1)).savePost(any(Post.class), any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void findFeed_Success() throws Exception {
        List<Post> mockPostList = new ArrayList<>();
        when(postService.findFeed()).thenReturn(mockPostList);

        mockMvc.perform(get("/posts")
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(postService, times(1)).findFeed();
    }

    @Test
    public void findAllPersonPosts_Success() throws Exception {
        List<Post> mockPostList = new ArrayList<>();
        when(postService.findAllPersonPosts(PERSON_ID)).thenReturn(mockPostList);

        mockMvc.perform(get("/posts/persons/{personId}", PERSON_ID)
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(postService, times(1)).findAllPersonPosts(PERSON_ID);
    }

    @Test
    public void findAllTagPosts_Success() throws Exception {
        when(postService.findAllTagPosts(TAG_TEXT_1)).thenReturn(mockTag1);

        mockMvc.perform(get("/posts/tags/{tagText}", TAG_TEXT_1)
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token))
                .andExpect(status().isOk());

        verify(postService, times(1)).findAllTagPosts(TAG_TEXT_1);
    }

    @Test(expected = NestedServletException.class)
    public void findPostById_WithInvalidId_ThrowsException() throws Exception {
        doThrow(new IllegalArgumentException()).when(postService).findPostById(POST_ID);

        mockMvc.perform(get("/posts/{postId}", POST_ID)
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token));
    }

    @Test
    public void findPostById_Success() throws Exception {
        when(postService.findPostById(POST_ID)).thenReturn(mockPost);

        mockMvc.perform(get("/posts/{postId}", POST_ID)
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(POST_DESCRIPTION));

        verify(postService, times(1)).findPostById(POST_ID);
    }

    @Test(expected = NestedServletException.class)
    public void deletePost_WithInvalidIdAndPerson_ThrowsException() throws Exception {
        doThrow(new IllegalArgumentException()).when(postService).deletePost(
                eq(POST_ID), any(UsernamePasswordAuthenticationToken.class));

        mockMvc.perform(delete("/posts/{postId}", POST_ID)
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token));
    }

    @Test
    public void deletePost_Success() throws Exception {
        doNothing().when(postService).deletePost(
                eq(POST_ID), any(UsernamePasswordAuthenticationToken.class));

        mockMvc.perform(delete("/posts/{postId}", POST_ID)
                .header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token))
                .andExpect(status().isOk());

        verify(postService, times(1)).deletePost(
                eq(POST_ID), any(UsernamePasswordAuthenticationToken.class));
    }

}
