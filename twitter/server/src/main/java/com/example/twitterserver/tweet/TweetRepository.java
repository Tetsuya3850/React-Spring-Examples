package com.example.twitterserver.tweet;

import com.example.twitterserver.user.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TweetRepository extends JpaRepository<Tweet, Long> {

    List<Tweet> findByApplicationUser(ApplicationUser applicationUser);

    List<Tweet> findByApplicationUserOrderByCreatedDesc(ApplicationUser applicationUser);

    Optional<Tweet> findByIdAndApplicationUser(Long tweetId, ApplicationUser applicationUser);
}
