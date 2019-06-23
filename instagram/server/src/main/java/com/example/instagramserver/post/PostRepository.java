package com.example.instagramserver.post;

import com.example.instagramserver.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByCreatedDesc();

    List<Post> findByPersonOrderByCreatedDesc(Person person);

    long deleteByIdAndPerson(Long id, Person person);
}
