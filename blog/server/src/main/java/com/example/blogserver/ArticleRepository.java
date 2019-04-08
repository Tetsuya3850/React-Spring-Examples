package com.example.blogserver;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByApplicationUser(ApplicationUser applicationUser);

    Page<Article> findAll(Pageable pageable);

    Article findByIdAndApplicationUser(Long id, ApplicationUser applicationUser);

}
