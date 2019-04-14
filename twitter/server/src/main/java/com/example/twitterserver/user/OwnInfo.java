package com.example.twitterserver.user;

import java.util.List;

public class OwnInfo {
    private List<Long> heartedTweetIds;
    private List<Long> followingUserIds;

    public OwnInfo(List<Long> heartedTweetIds, List<Long> followingUserIds) {
        this.heartedTweetIds = heartedTweetIds;
        this.followingUserIds = followingUserIds;
    }

    public List<Long> getHeartedTweetIds() {
        return heartedTweetIds;
    }

    public void setHeartedTweetIds(List<Long> heartedTweetIds) {
        this.heartedTweetIds = heartedTweetIds;
    }

    public List<Long> getFollowingUserIds() {
        return followingUserIds;
    }

    public void setFollowingUserIds(List<Long> followingUserIds) {
        this.followingUserIds = followingUserIds;
    }
}
