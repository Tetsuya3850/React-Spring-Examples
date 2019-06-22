package com.example.authserver;

import org.junit.BeforeClass;
import org.junit.Test;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static com.example.authserver.TestConstants.*;

public class PersonModelTest {

    private static Validator validator;

    @BeforeClass
    public static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void invalidWhen_UsernameIsNull() {
        Person person = new Person(null, PASSWORD);

        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertEquals(1, constraintViolations.size());
        assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void invalidWhen_UsernameIsEmptyString() {
        Person person = new Person("", PASSWORD);

        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertEquals(1, constraintViolations.size());
        assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void invalidWhen_UsernameIsOnlySpaces() {
        Person person = new Person(" ", PASSWORD);

        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertEquals(2, constraintViolations.size());
    }

    @Test
    public void invalidWhen_UsernameIsNotEmail() {
        Person person = new Person("me", PASSWORD);

        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertEquals(1, constraintViolations.size());
        assertEquals("must be a well-formed email address", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void invalidWhen_PasswordIsNull() {
        Person person = new Person(USERNAME, null);

        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void invalidWhen_PasswordIsShorterThan8() {
        Person person = new Person(USERNAME, TestUtils.dummyStringWithSpecifiedLength(7));

        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertEquals(1, constraintViolations.size());
        assertEquals("size must be between 8 and 2147483647", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void valid() {
        Person person = new Person(USERNAME, PASSWORD);

        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertEquals(0, constraintViolations.size());
    }

}
