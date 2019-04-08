package com.example.blogserver;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class ApplicationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(unique=true)
    @Size(min=1)
    private String username;

    @NotNull
    @Size(min=8)
    private String password;

    @OneToMany(mappedBy = "applicationUser", cascade = CascadeType.ALL)
    private List<Article> articles;

    public long getId() {
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
}