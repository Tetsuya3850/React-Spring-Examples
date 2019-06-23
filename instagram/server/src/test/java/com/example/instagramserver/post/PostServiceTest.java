package com.example.instagramserver.post;

import com.example.instagramserver.person.Person;
import com.example.instagramserver.person.PersonRepository;
import com.example.instagramserver.tag.Tag;
import com.example.instagramserver.tag.TagRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.instagramserver.commons.TestConstants.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PostServiceTest {

    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private PersonRepository personRepository;

    private Post mockPost;
    private Tag mockTag1;
    private Person mockPerson;
    private Authentication mockAuth;

    @Before
    public void setup(){
        postService = new PostService(postRepository, tagRepository, personRepository);
        mockPost = new Post(POST_ID, POST_DESCRIPTION);
        mockTag1 = new Tag(TAG_TEXT_1);
        mockPerson = new Person(PERSON_ID, USERNAME, PASSWORD);
        mockAuth = new UsernamePasswordAuthenticationToken(
                mockPerson.getUsername(), mockPerson.getPassword(), new ArrayList<>());
    }

    @Test
    public void savePost_Success(){
        class IsPostWithPerson implements ArgumentMatcher<Post> {
            public boolean matches(Post post) {
                return post.getPerson() != null;
            }
        }

        when(personRepository.findByUsername(mockPerson.getUsername())).thenReturn(Optional.of(mockPerson));
        when(postRepository.save(argThat(new IsPostWithPerson()))).thenReturn(mockPost);

        Post post = postService.savePost(mockPost, mockAuth);

        assertEquals(mockPost, post);
        verify(postRepository, times(1)).save(mockPost);
    }

    @Test
    public void findFeed_Success(){
        List<Post> mockPostList = new ArrayList<>();
        when(postRepository.findAllByOrderByCreatedDesc()).thenReturn(mockPostList);

        List<Post> postList = postService.findFeed();

        assertEquals(mockPostList, postList);
        verify(postRepository, times(1)).findAllByOrderByCreatedDesc();
    }

    @Test
    public void findAllPersonPosts_Success(){
        List<Post> mockPostList = new ArrayList<>();
        when(personRepository.findById(PERSON_ID)).thenReturn(Optional.of(mockPerson));
        when(postRepository.findByPersonOrderByCreatedDesc(mockPerson)).thenReturn(mockPostList);

        List<Post> postList = postService.findAllPersonPosts(PERSON_ID);

        assertEquals(mockPostList, postList);
        verify(postRepository, times(1)).findByPersonOrderByCreatedDesc(mockPerson);
    }

    @Test
    public void findAllTagPosts_Success(){
        when(tagRepository.findByText(TAG_TEXT_1)).thenReturn(mockTag1);

        Tag tag = postService.findAllTagPosts(TAG_TEXT_1);

        assertEquals(mockTag1, tag);
        verify(tagRepository, times(1)).findByText(TAG_TEXT_1);
    }

    @Test(expected = PostNotFoundException.class)
    public void findPostById_WithInvalidId_ThrowsException(){
        doThrow(new PostNotFoundException(POST_ID)).when(postRepository).findById(POST_ID);

        postService.findPostById(POST_ID);
    }

    @Test
    public void findPostById_Success(){
        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(mockPost));

        Post post = postService.findPostById(POST_ID);

        assertEquals(mockPost, post);
        verify(postRepository, times(1)).findById(POST_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deletePost_WithInvalidIdAndPerson_ThrowsException(){
        when(personRepository.findByUsername(mockPerson.getUsername())).thenReturn(Optional.of(mockPerson));
        when(postRepository.deleteByIdAndPerson(POST_ID, mockPerson)).thenReturn(0L);

        postService.deletePost(POST_ID, mockAuth);
    }

    @Test
    public void deletePost_Success(){
        when(personRepository.findByUsername(mockPerson.getUsername())).thenReturn(Optional.of(mockPerson));
        when(postRepository.deleteByIdAndPerson(POST_ID, mockPerson)).thenReturn(1L);

        postService.deletePost(POST_ID, mockAuth);

        verify(postRepository, times(1)).deleteByIdAndPerson(POST_ID, mockPerson);
    }


}
