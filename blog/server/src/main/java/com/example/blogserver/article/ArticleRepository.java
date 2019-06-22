package com.example.blogserver.article;

import com.example.blogserver.person.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findAllByOrderByCreatedDesc(Pageable pageable);

    List<Article> findByPersonOrderByCreatedDesc(Person person);

    Optional<Article> findByIdAndPerson(Long id, Person person);

    long deleteByIdAndPerson(Long id, Person person);

}
