package com.example.twitterserver.follow;

import com.example.twitterserver.user.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollower(ApplicationUser applicationUser);

    List<Follow> findByFollowee(ApplicationUser applicationUser);

    Follow findByFollowerAndFollowee(ApplicationUser follower, ApplicationUser followee);

}
