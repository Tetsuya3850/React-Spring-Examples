package com.example.twitterserver.heart;

import com.example.twitterserver.tweet.Tweet;
import com.example.twitterserver.user.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Heart findByApplicationUserAndTweet(ApplicationUser applicationUser, Tweet tweet);

    List<HeartTweetOnly> findByApplicationUser(ApplicationUser applicationUser);

    @Query("select h.tweet.id from Heart h where h.applicationUser = ?1")
    List<Long> getAllHeartedTweetIds(ApplicationUser applicationUser);
}
