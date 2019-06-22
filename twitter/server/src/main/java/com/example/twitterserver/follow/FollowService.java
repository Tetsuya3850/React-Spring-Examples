package com.example.twitterserver.follow;

import com.example.twitterserver.person.Person;
import com.example.twitterserver.person.PersonNotFoundException;
import com.example.twitterserver.person.PersonRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final PersonRepository personRepository;

    public FollowService(FollowRepository followRepository, PersonRepository personRepository){
        this.followRepository = followRepository;
        this.personRepository = personRepository;
    }

    void toggleFollow(Long followeeId, Authentication auth) {
        String username = auth.getName();
        Person follower = personRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        Person followee = personRepository
                .findById(followeeId)
                .orElseThrow(() -> new PersonNotFoundException(followeeId));
        Follow follow = followRepository.findByFollowerAndFollowee(follower, followee);
        if(follow == null){
            Follow newFollow = new Follow(follower, followee);
            follower.incrementFollowingCount();
            followee.incrementFollowersCount();
            followRepository.save(newFollow);
        } else {
            follower.decrementFollowingCount();
            followee.decrementFollowersCount();
            followRepository.delete(follow);
        }
        personRepository.save(follower);
        personRepository.save(followee);
    }

    List<Person> getAllFollowingByPersonId(Long personId){
        return followRepository.getAllFollowingByPersonId(personId);
    }

    List<Person> getAllFollowersByPersonId(Long personId){
        return followRepository.getAllFollowersByPersonId(personId);
    }

}
