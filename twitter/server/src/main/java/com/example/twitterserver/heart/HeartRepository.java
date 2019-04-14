package com.example.twitterserver.heart;

import com.example.twitterserver.tweet.Tweet;
import com.example.twitterserver.user.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Heart findByApplicationUserAndTweet(ApplicationUser applicationUser, Tweet tweet);

    @Query("select h.tweet from Heart h where h.applicationUser = ?1")
    List<Tweet> getByApplicationUser(ApplicationUser applicationUser);

    @Query("select h.applicationUser from Heart h where h.tweet.id = ?1")
    List<ApplicationUser> getByTweetId(Long id);

    @Query("select h.tweet.id from Heart h where h.applicationUser = ?1")
    List<Long> getAllHeartedTweetIds(ApplicationUser applicationUser);


}
