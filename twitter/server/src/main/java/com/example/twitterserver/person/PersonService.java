package com.example.twitterserver.person;

import com.example.twitterserver.follow.FollowRepository;
import com.example.twitterserver.heart.HeartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final HeartRepository heartRepository;
    private final FollowRepository followRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public PersonService(PersonRepository personRepository,
                         HeartRepository heartRepository,
                         FollowRepository followRepository,
                         BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.personRepository = personRepository;
        this.heartRepository = heartRepository;
        this.followRepository = followRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    Person savePerson(Person person) {
        person.setPassword(bCryptPasswordEncoder.encode(person.getPassword()));
        return personRepository.save(person);
    }

    List<Person> findAllPersons(){
        return personRepository.findAll();
    }

    Person findPersonById(Long personId) {
        return personRepository
                .findById(personId)
                .orElseThrow(() -> new PersonNotFoundException(personId));
    }

    OwnInfo getOwnInfo(Authentication auth){
        String username = auth.getName();
        Person person = personRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        List<Long> heartedTweetIds = heartRepository.getAllHeartedTweetIdsByPerson(person);
        List<Long> followingUserIds = followRepository.getAllFollowingPersonIdsByPerson(person);
        return new OwnInfo(heartedTweetIds, followingUserIds);
    }
}
