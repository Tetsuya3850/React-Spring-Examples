package com.example.authserver;

import org.junit.BeforeClass;
import org.junit.Test;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Iterator;
import java.util.Set;
import static org.junit.Assert.assertEquals;

public class PersonModelTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void usernameIsNull() {
        Person person = new Person(null, TestConstants.password);

        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertEquals(1, constraintViolations.size());
        assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void usernameIsEmptyString() {
        Person person = new Person("", TestConstants.password);

        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertEquals(1, constraintViolations.size());
        assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void usernameIsOnlySpaces() {
        Person person = new Person("    ", TestConstants.password);

        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);
        Iterator<ConstraintViolation<Person>> constraintViolationsIterator = constraintViolations.iterator();

        assertEquals(2, constraintViolations.size());
        assertEquals("must not be blank", constraintViolationsIterator.next().getMessage());
        assertEquals("must be a well-formed email address", constraintViolationsIterator.next().getMessage());
    }

    @Test
    public void usernameIsNotEmail() {
        Person person = new Person("me", TestConstants.password);

        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertEquals(1, constraintViolations.size());
        assertEquals("must be a well-formed email address", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void passwordIsNull() {
        Person person = new Person(TestConstants.username, null);

        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void passwordIsShorterThen8() {
        Person person = new Person(TestConstants.username, "1234567");

        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertEquals(1, constraintViolations.size());
        assertEquals("size must be between 8 and 2147483647", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void personIsValid() {
        Person person = new Person(TestConstants.username, TestConstants.password);

        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertEquals(0, constraintViolations.size());
    }

}
