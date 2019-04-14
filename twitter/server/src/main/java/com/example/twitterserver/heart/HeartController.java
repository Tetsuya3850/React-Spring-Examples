package com.example.twitterserver.heart;

import com.example.twitterserver.tweet.Tweet;
import com.example.twitterserver.tweet.TweetNotFoundException;
import com.example.twitterserver.tweet.TweetRepository;
import com.example.twitterserver.user.ApplicationUser;
import com.example.twitterserver.user.ApplicationUserNotFoundException;
import com.example.twitterserver.user.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hearts")
public class HeartController {
    @Autowired
    private HeartRepository heartRepository;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private TweetRepository tweetRepository;

    @PostMapping("/{tweetId}")
    void toggleHeart(@PathVariable Long tweetId, Authentication authentication) {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(authentication.getName());
        Tweet tweet = tweetRepository.findById(tweetId).orElseThrow(() -> new TweetNotFoundException(tweetId));
        Heart heart = heartRepository.findByApplicationUserAndTweet(applicationUser, tweet);
        if(heart == null){
            Heart newLike = new Heart(applicationUser, tweet);
            heartRepository.save(newLike);
            tweet.incrementHeartCount();
        } else {
            heartRepository.delete(heart);
            tweet.decrementHeartCount();
        }
        tweetRepository.save(tweet);
    }

    @GetMapping("/users/{userId}")
    List<HeartTweetOnly> getHeartedTweets(@PathVariable Long userId){
        ApplicationUser applicationUser = applicationUserRepository.findById(userId).orElseThrow(() -> new ApplicationUserNotFoundException(userId));
        return heartRepository.findByApplicationUser(applicationUser);
    }
}
