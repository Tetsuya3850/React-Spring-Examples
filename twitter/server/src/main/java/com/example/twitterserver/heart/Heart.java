package com.example.twitterserver.heart;

import com.example.twitterserver.tweet.Tweet;
import com.example.twitterserver.user.ApplicationUser;

import javax.persistence.*;

@Entity
public class Heart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="application_user_id")
    private ApplicationUser applicationUser;

    @ManyToOne
    @JoinColumn(name="tweet_id")
    private Tweet tweet;

    public Heart() {
    }

    public Heart(ApplicationUser applicationUser, Tweet tweet) {
        this.applicationUser = applicationUser;
        this.tweet = tweet;
    }

    public Long getId() {
        return id;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    public Tweet getTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }
}

