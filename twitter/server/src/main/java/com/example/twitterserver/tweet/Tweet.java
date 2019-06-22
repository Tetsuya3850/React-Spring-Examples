package com.example.twitterserver.tweet;

import com.example.twitterserver.person.Person;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Entity
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(max=140)
    private String text;

    @CreationTimestamp
    private Timestamp created;

    @ManyToOne
    @JoinColumn(name="person_id")
    private Person person;

    @ColumnDefault("0")
    private int heartCount;

    public Tweet() {
    }

    public Tweet(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getCreated() {
        return created;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public int getHeartCount() {
        return heartCount;
    }

    public void incrementHeartCount() {
        this.heartCount += 1;
    }

    public void decrementHeartCount() {
        this.heartCount -= 1;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", created=" + created +
                ", person=" + person +
                ", heartCount=" + heartCount +
                '}';
    }

}
