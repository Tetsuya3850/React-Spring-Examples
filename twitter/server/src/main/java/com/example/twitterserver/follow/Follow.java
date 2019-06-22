package com.example.twitterserver.follow;

import com.example.twitterserver.person.Person;

import javax.persistence.*;

@Table(uniqueConstraints= @UniqueConstraint(columnNames={"follower_id", "followee_id"}))
@Entity
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="follower_id")
    private Person follower;

    @ManyToOne
    @JoinColumn(name="followee_id")
    private Person followee;

    public Follow() {
    }

    public Follow(Person follower, Person followee) {
        this.follower = follower;
        this.followee = followee;
    }

    public Long getId() {
        return id;
    }

    public Person getFollower() {
        return follower;
    }

    public void setFollower(Person follower) {
        this.follower = follower;
    }

    public Person getFollowee() {
        return followee;
    }

    public void setFollowee(Person followee) {
        this.followee = followee;
    }

    @Override
    public String toString() {
        return "Follow{" +
                "id=" + id +
                ", follower=" + follower +
                ", followee=" + followee +
                '}';
    }

}
