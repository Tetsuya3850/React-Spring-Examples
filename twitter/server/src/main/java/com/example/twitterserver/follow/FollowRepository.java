package com.example.twitterserver.follow;

import com.example.twitterserver.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Follow findByFollowerAndFollowee(Person follower, Person followee);

    @Query("select f.follower from Follow f where f.followee.id = :personId")
    List<Person> getAllFollowersByPersonId(@Param("personId") Long personId);

    @Query("select f.followee from Follow f where f.follower.id = :personId")
    List<Person> getAllFollowingByPersonId(@Param("personId") Long personId);

    @Query("select f.followee.id from Follow f where f.follower = :person")
    List<Long> getAllFollowingPersonIdsByPerson(@Param("person") Person person);

}
