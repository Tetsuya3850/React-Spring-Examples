package com.example.twitterserver.heart;

import com.example.twitterserver.person.Person;
import com.example.twitterserver.tweet.Tweet;

import javax.persistence.*;

@Table(uniqueConstraints= @UniqueConstraint(columnNames={"person_id", "tweet_id"}))
@Entity
public class Heart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="person_id")
    private Person person;

    @ManyToOne
    @JoinColumn(name="tweet_id")
    private Tweet tweet;

    public Heart() {
    }

    public Heart(Person person, Tweet tweet) {
        this.person = person;
        this.tweet = tweet;
    }

    public Long getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Tweet getTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }

    @Override
    public String toString() {
        return "Heart{" +
                "id=" + id +
                ", person=" + person +
                ", tweet=" + tweet +
                '}';
    }

}

