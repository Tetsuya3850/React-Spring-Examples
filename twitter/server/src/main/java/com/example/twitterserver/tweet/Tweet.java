package com.example.twitterserver.tweet;

import com.example.twitterserver.user.ApplicationUser;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Entity
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min=1, max=140)
    private String text;

    private String created = new SimpleDateFormat("yyyyMMdd_HHmmss.SSS").format(Calendar.getInstance().getTime());

    @ManyToOne
    @JoinColumn(name="application_user_id")
    private ApplicationUser applicationUser;

    private int heartCount = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreated() {
        return created;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
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

}
