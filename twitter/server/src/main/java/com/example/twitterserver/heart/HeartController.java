package com.example.twitterserver.heart;

import com.example.twitterserver.person.Person;
import com.example.twitterserver.tweet.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hearts")
public class HeartController {

    private final HeartService heartService;

    @Autowired
    public HeartController(HeartService heartService){
        this.heartService = heartService;
    }

    @PostMapping("/{tweetId}")
    void toggleHeart(@PathVariable Long tweetId, Authentication auth) {
        heartService.toggleHeart(tweetId, auth);
    }

    @GetMapping("/persons/{personId}")
    List<Tweet> getAllHeartedTweetsByPersonId(@PathVariable Long personId){
        return heartService.getAllHeartedTweetsByPersonId(personId);
    }

    @GetMapping("/tweets/{tweetId}")
    List<Person> getAllHeartedPersonsByTweetId(@PathVariable Long tweetId){
        return heartService.getAllHeartedPersonsByTweetId(tweetId);
    }
}
