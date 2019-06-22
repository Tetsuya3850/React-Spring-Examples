package com.example.twitterserver.heart;

import com.example.twitterserver.person.Person;
import com.example.twitterserver.tweet.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HeartRepository extends JpaRepository<Heart, Long> {

    Heart findByPersonAndTweet(Person person, Tweet tweet);

    List<Heart> removeByTweet(Tweet tweet);

    @Query("select h.person from Heart h where h.tweet.id = :tweetId")
    List<Person> getAllHeartedPersonsByTweetId(@Param("tweetId") Long tweetId);

    @Query("select h.tweet from Heart h where h.person.id = :personId")
    List<Tweet> getAllHeartedTweetsByPersonId(@Param("personId") Long personId);

    @Query("select h.tweet.id from Heart h where h.person = :person")
    List<Long> getAllHeartedTweetIdsByPerson(@Param("person") Person person);

}
