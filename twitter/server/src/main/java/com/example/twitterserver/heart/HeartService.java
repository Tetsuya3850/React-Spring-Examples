package com.example.twitterserver.heart;

import com.example.twitterserver.person.Person;
import com.example.twitterserver.person.PersonRepository;
import com.example.twitterserver.tweet.Tweet;
import com.example.twitterserver.tweet.TweetNotFoundException;
import com.example.twitterserver.tweet.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeartService {

    private final HeartRepository heartRepository;
    private final PersonRepository personRepository;
    private final TweetRepository tweetRepository;

    @Autowired
    public HeartService(HeartRepository heartRepository,
                        PersonRepository personRepository,
                        TweetRepository tweetRepository){
        this.heartRepository = heartRepository;
        this.personRepository = personRepository;
        this.tweetRepository = tweetRepository;
    }

    void toggleHeart(Long tweetId, Authentication auth) {
        String username = auth.getName();
        Person person = personRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        Tweet tweet = tweetRepository
                .findById(tweetId)
                .orElseThrow(() -> new TweetNotFoundException(tweetId));
        Heart heart = heartRepository.findByPersonAndTweet(person, tweet);
        if(heart == null){
            Heart newHeart = new Heart(person, tweet);
            heartRepository.save(newHeart);
            tweet.incrementHeartCount();
        } else {
            heartRepository.delete(heart);
            tweet.decrementHeartCount();
        }
        tweetRepository.save(tweet);
    }

    List<Person> getAllHeartedPersonsByTweetId(Long tweetId){
        return heartRepository.getAllHeartedPersonsByTweetId(tweetId);
    }

    List<Tweet> getAllHeartedTweetsByPersonId(Long personId){
        return heartRepository.getAllHeartedTweetsByPersonId(personId);
    }

}
