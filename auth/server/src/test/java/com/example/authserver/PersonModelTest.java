package com.example.authserver;

import org.junit.BeforeClass;
import org.junit.Test;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class PersonModelTest {

    private static Validator validator;

    private final String username = "me@gmail.com";
    private final String password = "Test3850";

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void usernameIsNull() {
        Person person = new Person(null, password);

        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertEquals(1, constraintViolations.size());
        assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void usernameIsEmptyString() {
        Person person = new Person("", password);

        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertEquals(1, constraintViolations.size());
        assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void usernameIsOnlySpaces() {
        Person person = new Person(" ", password);

        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertEquals(2, constraintViolations.size());
    }

    @Test
    public void usernameIsNotEmail() {
        Person person = new Person("me", password);

        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertEquals(1, constraintViolations.size());
        assertEquals("must be a well-formed email address", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void passwordIsNull() {
        Person person = new Person(username, null);

        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void passwordIsShorterThen8() {
        Person person = new Person(username, TestUtils.dummyStringWithSpecifiedLength(7));

        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertEquals(1, constraintViolations.size());
        assertEquals("size must be between 8 and 2147483647", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void personIsValid() {
        Person person = new Person(username, password);

        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertEquals(0, constraintViolations.size());
    }

}
