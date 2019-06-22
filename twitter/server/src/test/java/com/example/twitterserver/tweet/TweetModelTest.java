package com.example.twitterserver.tweet;

import com.example.twitterserver.commons.TestUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static com.example.twitterserver.commons.TestConstants.*;
import static org.junit.Assert.assertEquals;

public class TweetModelTest {

    private static Validator validator;

    @BeforeClass
    public static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void invalidWhen_TextIsNull() {
        Tweet tweet = new Tweet(null);

        Set<ConstraintViolation<Tweet>> constraintViolations = validator.validate(tweet);

        assertEquals(1, constraintViolations.size());
        assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void invalidWhen_TextIsEmptyString() {
        Tweet tweet = new Tweet("");

        Set<ConstraintViolation<Tweet>> constraintViolations = validator.validate(tweet);

        assertEquals(1, constraintViolations.size());
        assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void invalidWhen_TextIsOnlySpaces() {
        Tweet tweet = new Tweet(" ");

        Set<ConstraintViolation<Tweet>> constraintViolations = validator.validate(tweet);

        assertEquals(1, constraintViolations.size());
        assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void invalidWhen_TextIsLongerThan140() {
        Tweet tweet = new Tweet(TestUtils.dummyStringWithSpecifiedLength(141));

        Set<ConstraintViolation<Tweet>> constraintViolations = validator.validate(tweet);

        assertEquals(1, constraintViolations.size());
        assertEquals("size must be between 0 and 140", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void valid() {
        Tweet tweet = new Tweet(TWEET_TEXT);

        Set<ConstraintViolation<Tweet>> constraintViolations = validator.validate(tweet);

        assertEquals(0, constraintViolations.size());
    }

}
