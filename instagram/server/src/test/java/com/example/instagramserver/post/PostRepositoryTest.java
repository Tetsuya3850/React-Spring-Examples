package com.example.instagramserver.post;

import com.example.instagramserver.person.Person;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.example.instagramserver.commons.TestConstants.*;
import static org.junit.Assert.assertEquals;

@DataJpaTest
@RunWith(SpringRunner.class)
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Person person;
    private Person another_person;
    private Post post;
    private Long postId;
    private Post another_post;

    @Before
    public void setup(){
        person = testEntityManager.persistFlushFind(new Person(PERSON_ID, USERNAME, PASSWORD));
        another_person = testEntityManager.persistFlushFind(new Person(ANOTHER_PERSON_ID, ANOTHER_USERNAME, PASSWORD));
        post = new Post(POST_ID, POST_DESCRIPTION);
        post.setPerson(person);
        post = testEntityManager.persistFlushFind(post);
        postId = post.getId();
        another_post = new Post(ANOTHER_POST_ID, POST_DESCRIPTION);
        another_post.setPerson(person);
        another_post = testEntityManager.persistFlushFind(another_post);
    }

    @Test
    public void getFeed_Success(){
        List<Post> postList = postRepository.findAllByOrderByCreatedDesc();
        assertEquals(2, postList.size());
        assertEquals(another_post, postList.get(0));
    }

    @Test
    public void findByPersonOrderByCreatedDesc_Success(){
        List<Post> postList = postRepository.findByPersonOrderByCreatedDesc(person);
        assertEquals(2, postList.size());
        assertEquals(another_post, postList.get(0));
        List<Post> postListAnother = postRepository.findByPersonOrderByCreatedDesc(another_person);
        assertEquals(0, postListAnother.size());
    }

    @Test
    public void deleteByIdAndPerson_WithNonOwnerPerson_DeletesNothing(){
        long result = postRepository.deleteByIdAndPerson(postId, another_person);
        assertEquals(0L, result);
    }

    @Test
    public void deleteByIdAndPerson_Success(){
        long result = postRepository.deleteByIdAndPerson(postId, person);
        assertEquals(1L, result);
    }

}
