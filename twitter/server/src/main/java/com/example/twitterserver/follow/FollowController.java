package com.example.twitterserver.follow;

import com.example.twitterserver.user.ApplicationUser;
import com.example.twitterserver.user.ApplicationUserNotFoundException;
import com.example.twitterserver.user.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follows")
public class FollowController {
    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @PostMapping("/{followeeId}")
    void toggleFollow(@PathVariable Long followeeId, Authentication authentication) {
        ApplicationUser follower = applicationUserRepository.findByUsername(authentication.getName());
        ApplicationUser followee = applicationUserRepository.findById(followeeId).orElseThrow(() -> new ApplicationUserNotFoundException(followeeId));
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
        applicationUserRepository.save(follower);
        applicationUserRepository.save(followee);
    }

    @GetMapping("/following/{followerId}")
    List<Follow> getFollowing(@PathVariable Long followerId){
        ApplicationUser follower = applicationUserRepository.findById(followerId).orElseThrow(() -> new ApplicationUserNotFoundException(followerId));
        return followRepository.findByFollower(follower);
    }

    @GetMapping("/followers/{followeeId}")
    List<Follow> getFollowers(@PathVariable Long followeeId){
        ApplicationUser followee = applicationUserRepository.findById(followeeId).orElseThrow(() -> new ApplicationUserNotFoundException(followeeId));
        return followRepository.findByFollowee(followee);
    }
}
