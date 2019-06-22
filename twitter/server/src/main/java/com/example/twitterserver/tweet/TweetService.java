package com.example.twitterserver.tweet;

import com.example.twitterserver.heart.HeartRepository;
import com.example.twitterserver.person.Person;
import com.example.twitterserver.person.PersonNotFoundException;
import com.example.twitterserver.person.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final PersonRepository personRepository;
    private final HeartRepository heartRepository;

    @Autowired
    public TweetService(TweetRepository tweetRepository,
                        PersonRepository personRepository,
                        HeartRepository heartRepository){
        this.tweetRepository = tweetRepository;
        this.personRepository = personRepository;
        this.heartRepository = heartRepository;
    }

    Tweet saveTweet(Tweet newTweet, Authentication auth) {
        String username = auth.getName();
        Person person = personRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        newTweet.setPerson(person);
        return tweetRepository.save(newTweet);
    }

    List<Tweet> getFeed(Authentication auth) {
        String username = auth.getName();
        Person person = personRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return tweetRepository.getFeed(person.getId());
    }

    List<Tweet> findAllPersonTweets(Long personId) {
        Person person = personRepository
                .findById(personId)
                .orElseThrow(() -> new PersonNotFoundException(personId));
        return tweetRepository.findByPersonOrderByCreatedDesc(person);
    }

    Tweet findTweetById(Long tweetId) {
        return tweetRepository
                .findById(tweetId)
                .orElseThrow(() -> new TweetNotFoundException(tweetId));
    }

    @Transactional
    public void deleteTweet(Long tweetId, Authentication auth) {
        String username = auth.getName();
        Person person = personRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        Tweet tweet = tweetRepository
                .findById(tweetId)
                .orElseThrow(() -> new TweetNotFoundException(tweetId));
        heartRepository.removeByTweet(tweet);
        long result = tweetRepository.deleteByIdAndPerson(tweetId, person);
        if(result == 0L){
            throw new IllegalArgumentException();
        }
    }

}
