package com.example.instagramserver.post;

import com.example.instagramserver.tag.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("")
    Post savePost(@Valid @RequestBody Post newPost, Authentication auth) {
        return postService.savePost(newPost, auth);
    }

    @GetMapping("")
    List<Post> findFeed() {
        return postService.findFeed();
    }

    @GetMapping("/persons/{personId}")
    List<Post> findAllPersonPosts(@PathVariable Long personId) {
        return postService.findAllPersonPosts(personId);
    }

    @GetMapping("/tags/{tagText}")
    Tag findAllTagPosts(@PathVariable String tagText) {
        return postService.findAllTagPosts(tagText);
    }

    @GetMapping("/{postId}")
    Post findPostById(@PathVariable Long postId) {
        return postService.findPostById(postId);
    }

    @DeleteMapping("/{postId}")
    void deletePost(@PathVariable Long postId, Authentication auth) {
        postService.deletePost(postId, auth);
    }

}
