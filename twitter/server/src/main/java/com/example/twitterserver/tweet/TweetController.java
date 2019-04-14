package com.example.twitterserver.tweet;

import com.example.twitterserver.follow.Follow;
import com.example.twitterserver.follow.FollowRepository;
import com.example.twitterserver.user.ApplicationUser;
import com.example.twitterserver.user.ApplicationUserNotFoundException;
import com.example.twitterserver.user.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/tweets")
public class TweetController {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private FollowRepository followRepository;

    @PostMapping("")
    Tweet postTweet(@Valid @RequestBody Tweet newTweet, Authentication authentication) {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(authentication.getName());
        newTweet.setApplicationUser(applicationUser);
        return tweetRepository.save(newTweet);
    }

    @GetMapping("")
    List<Tweet> getFeed(Authentication auth) {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(auth.getName());
        List<Follow> following = followRepository.findByFollower(applicationUser);
        List<Tweet> feed = new ArrayList<Tweet>();
        for(Follow follow : following){
            feed.addAll(tweetRepository.findByApplicationUser(follow.getFollowee()));
        }
        feed.addAll(tweetRepository.findByApplicationUser(applicationUser));
        feed.sort(Comparator.comparing(Tweet::getCreated).reversed());
        return feed;
    }

    @GetMapping("/users/{userId}")
    List<Tweet> getAllUserArticles(@PathVariable Long userId) {
        ApplicationUser applicationUser = applicationUserRepository.findById(userId)
                .orElseThrow(() -> new ApplicationUserNotFoundException(userId));
        return tweetRepository.findByApplicationUserOrderByCreatedDesc(applicationUser);
    }

    @GetMapping("/{tweetId}")
    Tweet getTweet(@PathVariable Long tweetId) {
        return tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TweetNotFoundException(tweetId));
    }

    @DeleteMapping("/{tweetId}")
    void deleteArticle(@PathVariable Long tweetId, Authentication auth) {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(auth.getName());
        Tweet tweet = tweetRepository.findByIdAndApplicationUser(tweetId, applicationUser).orElseThrow(() -> new TweetNotFoundException(tweetId));
        tweetRepository.delete(tweet);
    }

}
