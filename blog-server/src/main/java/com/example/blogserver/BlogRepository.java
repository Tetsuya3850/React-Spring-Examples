package com.example.blogserver;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByApplicationUser(ApplicationUser applicationUser);

}
