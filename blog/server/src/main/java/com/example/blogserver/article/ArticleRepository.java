package com.example.blogserver.article;

import com.example.blogserver.user.ApplicationUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByApplicationUserOrderByCreatedDesc(ApplicationUser applicationUser);

    Page<Article> findAllByOrderByCreatedDesc(Pageable pageable);

    Optional<Article> findByIdAndApplicationUser(Long id, ApplicationUser applicationUser);

}
