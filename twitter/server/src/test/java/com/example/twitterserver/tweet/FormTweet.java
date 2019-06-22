package com.example.twitterserver.tweet;

public class FormTweet {
    private String text;

    public FormTweet() {
    }

    public FormTweet(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
