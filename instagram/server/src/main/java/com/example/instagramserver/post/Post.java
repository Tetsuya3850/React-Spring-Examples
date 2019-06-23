package com.example.instagramserver.post;

import com.example.instagramserver.person.Person;
import com.example.instagramserver.tag.Tag;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Post {

    @Id
    private Long id;

    @NotNull
    private String imgPath;

    private String description;

    @CreationTimestamp
    private Timestamp created;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties("posts")
    private List<Tag> tags = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="person_id")
    private Person person;

    public Post() {
    }

    public Post(Long id, String imgPath, String description) {
        this.id = id;
        this.imgPath = imgPath;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void addTag(Tag tag) {
        tags.add(tag);
        tag.getPosts().add(this);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
        tag.getPosts().remove(this);
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

}
