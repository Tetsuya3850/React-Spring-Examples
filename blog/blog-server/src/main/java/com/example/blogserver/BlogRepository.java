package com.example.blogserver;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByApplicationUser(ApplicationUser applicationUser);

    Page<Blog> findAll(Pageable pageable);

    Blog findByIdAndApplicationUser(Long id, ApplicationUser applicationUser);

}
