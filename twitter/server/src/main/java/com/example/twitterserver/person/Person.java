package com.example.twitterserver.person;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Email
    @Column(unique = true)
    private String username;

    @NotNull
    @Size(min = 8)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ColumnDefault("0")
    private int followingCount;

    @ColumnDefault("0")
    private int followersCount;

    public Person() {
    }

    public Person(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", followingCount=" + followingCount +
                ", followersCount=" + followersCount +
                '}';
    }
}