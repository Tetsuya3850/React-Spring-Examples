package com.example.twitterserver.user;

import com.example.twitterserver.follow.FollowRepository;
import com.example.twitterserver.heart.HeartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private ApplicationUserRepository applicationUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private HeartRepository heartRepository;

    public UserController(ApplicationUserRepository applicationUserRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/signup")
    public void signUp(@RequestBody ApplicationUser user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        applicationUserRepository.save(user);
    }

    @GetMapping("")
    List<ApplicationUser> getUsers() {
        return applicationUserRepository.findAll();
    }

    @GetMapping("/{userId}")
    ApplicationUser getUser(@PathVariable Long userId) {
        return applicationUserRepository.findById(userId)
                .orElseThrow(() -> new ApplicationUserNotFoundException(userId));
    }

    @GetMapping("/me")
    OwnInfo getOwnInfo(Authentication auth){
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(auth.getName());
        List<Long> heartedTweetIds = heartRepository.getAllHeartedTweetIds(applicationUser);
        List<Long> followingUserIds = followRepository.getAllFollowingUserIds(applicationUser);
        return new OwnInfo(heartedTweetIds, followingUserIds);
    }
}