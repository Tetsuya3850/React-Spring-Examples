package com.example.twitterserver.tweet;

import com.example.twitterserver.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TweetRepository extends JpaRepository<Tweet, Long> {

    @Query(value = "SELECT tweet.*, person.* FROM tweet " +
            "INNER JOIN person ON tweet.person_id = person.id " +
            "LEFT OUTER JOIN follow ON follow.followee_id = person.id " +
            "WHERE follow.follower_id = :personId OR tweet.person_id = :personId " +
            "ORDER BY tweet.created DESC", nativeQuery = true)
    List<Tweet> getFeed(@Param("personId") Long personId);

    List<Tweet> findByPersonOrderByCreatedDesc(Person person);

    long deleteByIdAndPerson(Long id, Person person);

}
