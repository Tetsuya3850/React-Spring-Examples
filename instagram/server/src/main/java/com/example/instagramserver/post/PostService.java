package com.example.instagramserver.post;

import com.example.instagramserver.person.Person;
import com.example.instagramserver.person.PersonNotFoundException;
import com.example.instagramserver.person.PersonRepository;
import com.example.instagramserver.tag.Tag;
import com.example.instagramserver.tag.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PersonRepository personRepository;

    @Autowired
    public PostService(PostRepository postRepository,
                       TagRepository tagRepository,
                       PersonRepository personRepository){
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.personRepository = personRepository;
    }

    Post savePost(Post newPost, Authentication auth) {
        String username = auth.getName();
        Person person = personRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        newPost.setPerson(person);
        return postRepository.save(newPost);
    }

    List<Post> findFeed() {
        return postRepository.findAllByOrderByCreatedDesc();
    }

    List<Post> findAllPersonPosts(Long personId) {
        Person person = personRepository
                .findById(personId)
                .orElseThrow(() -> new PersonNotFoundException(personId));
        return postRepository.findByPersonOrderByCreatedDesc(person);
    }

    Tag findAllTagPosts(String tagText) {
        return tagRepository.findByText(tagText);
    }

    Post findPostById(Long postId) {
        return postRepository
                .findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
    }

    @Transactional
    public void deletePost(Long postId, Authentication auth) {
        String username = auth.getName();
        Person person = personRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        long result = postRepository.deleteByIdAndPerson(postId, person);
        if(result == 0L){
            throw new IllegalArgumentException();
        }
    }

}
