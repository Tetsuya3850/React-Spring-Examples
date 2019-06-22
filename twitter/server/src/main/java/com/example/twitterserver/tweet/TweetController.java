package com.example.twitterserver.tweet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tweets")
public class TweetController {

    private final TweetService tweetService;

    @Autowired
    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @PostMapping("")
    Tweet saveTweet(@Valid @RequestBody Tweet newTweet, Authentication auth) {
        return tweetService.saveTweet(newTweet, auth);
    }

    @GetMapping("")
    List<Tweet> getFeed(Authentication auth) {
        return tweetService.getFeed(auth);
    }

    @GetMapping("/persons/{personId}")
    List<Tweet> findAllPersonTweets(@PathVariable Long personId) {
        return tweetService.findAllPersonTweets(personId);
    }

    @GetMapping("/{tweetId}")
    Tweet findTweetById(@PathVariable Long tweetId) {
        return tweetService.findTweetById(tweetId);
    }

    @DeleteMapping("/{tweetId}")
    void deleteTweet(@PathVariable Long tweetId, Authentication auth) {
        tweetService.deleteTweet(tweetId, auth);
    }
}
