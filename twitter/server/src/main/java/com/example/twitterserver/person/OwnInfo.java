package com.example.twitterserver.person;

import java.util.List;

public class OwnInfo {
    private List<Long> heartedTweetIds;
    private List<Long> followingPersonIds;

    public OwnInfo() {
    }

    public OwnInfo(List<Long> heartedTweetIds, List<Long> followingPersonIds) {
        this.heartedTweetIds = heartedTweetIds;
        this.followingPersonIds = followingPersonIds;
    }

    public List<Long> getHeartedTweetIds() {
        return heartedTweetIds;
    }

    public void setHeartedTweetIds(List<Long> heartedTweetIds) {
        this.heartedTweetIds = heartedTweetIds;
    }

    public List<Long> getFollowingPersonIds() {
        return followingPersonIds;
    }

    public void setFollowingPersonIds(List<Long> followingPersonIds) {
        this.followingPersonIds = followingPersonIds;
    }

}
