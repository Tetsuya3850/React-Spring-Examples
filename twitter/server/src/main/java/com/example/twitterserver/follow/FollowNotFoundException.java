package com.example.twitterserver.follow;

public class FollowNotFoundException extends RuntimeException {
    public FollowNotFoundException() {
        super("Could not find follow");
    }
}
