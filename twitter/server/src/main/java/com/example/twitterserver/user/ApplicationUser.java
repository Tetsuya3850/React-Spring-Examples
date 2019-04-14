package com.example.twitterserver.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class ApplicationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique=true)
    @Size(min=1)
    private String username;

    @NotNull
    @Size(min=8)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private int followingCount = 0;

    private int followersCount = 0;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void incrementFollowingCount() {
        this.followingCount += 1;
    }

    public void decrementFollowingCount() {
        this.followingCount -= 1;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void incrementFollowersCount() {
        this.followersCount += 1;
    }

    public void decrementFollowersCount() {
        this.followersCount -= 1;
    }
}