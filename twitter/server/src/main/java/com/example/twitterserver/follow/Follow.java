package com.example.twitterserver.follow;

import com.example.twitterserver.user.ApplicationUser;

import javax.persistence.*;

@Entity
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="follower_id")
    private ApplicationUser follower;

    @ManyToOne
    @JoinColumn(name="followee_id")
    private ApplicationUser followee;

    public Follow() {
    }

    public Follow(ApplicationUser follower, ApplicationUser followee) {
        this.follower = follower;
        this.followee = followee;
    }

    public Long getId() {
        return id;
    }

    public ApplicationUser getFollower() {
        return follower;
    }

    public void setFollower(ApplicationUser follower) {
        this.follower = follower;
    }

    public ApplicationUser getFollowee() {
        return followee;
    }

    public void setFollowee(ApplicationUser followee) {
        this.followee = followee;
    }
}
