package com.example.twitterserver.follow;

import com.example.twitterserver.person.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follows")
public class FollowController {

    private final FollowService followService;

    @Autowired
    public FollowController( FollowService followService){
        this.followService = followService;
    }

    @PostMapping("/{followeeId}")
    void toggleFollow(@PathVariable Long followeeId, Authentication auth) {
        followService.toggleFollow(followeeId, auth);
    }

    @GetMapping("/following/{personId}")
    List<Person> getAllFollowingByPersonId(@PathVariable Long personId){
        return followService.getAllFollowingByPersonId(personId);
    }

    @GetMapping("/followers/{personId}")
    List<Person> getAllFollowersByPersonId(@PathVariable Long personId){
        return followService.getAllFollowersByPersonId(personId);
    }
}
