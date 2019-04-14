package com.example.twitterserver.heart;

import com.example.twitterserver.tweet.Tweet;
import com.example.twitterserver.user.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Heart findByApplicationUserAndTweet(ApplicationUser applicationUser, Tweet tweet);

    List<Heart> findByApplicationUser(ApplicationUser applicationUser);
}
