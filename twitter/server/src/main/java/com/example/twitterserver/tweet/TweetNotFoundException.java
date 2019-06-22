package com.example.twitterserver.tweet;

public class TweetNotFoundException extends RuntimeException {
    public TweetNotFoundException(Long id) {
        super("Could not find tweet " + id);
    }
}
