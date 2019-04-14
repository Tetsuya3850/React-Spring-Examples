package com.example.twitterserver.follow;

import com.example.twitterserver.user.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollower(ApplicationUser applicationUser);

    List<Follow> findByFollowee(ApplicationUser applicationUser);

    Follow findByFollowerAndFollowee(ApplicationUser follower, ApplicationUser followee);

    @Query("select f.followee.id from Follow f where f.follower = ?1")
    List<Long> getAllFollowingUserIds(ApplicationUser applicationUser);

}
