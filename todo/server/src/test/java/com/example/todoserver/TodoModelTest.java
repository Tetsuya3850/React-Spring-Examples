package com.example.todoserver;

import org.junit.BeforeClass;
import org.junit.Test;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import static org.junit.Assert.assertEquals;

public class TodoModelTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void textIsNull() {
        Todo todo = new Todo( null);

        Set<ConstraintViolation<Todo>> constraintViolations = validator.validate(todo);

        assertEquals(1, constraintViolations.size());
        assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void textIsEmptyString() {
        Todo todo = new Todo( "");

        Set<ConstraintViolation<Todo>> constraintViolations = validator.validate(todo);

        assertEquals(1, constraintViolations.size());
        assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void textIsOnlySpaces() {
        Todo todo = new Todo( " ");

        Set<ConstraintViolation<Todo>> constraintViolations = validator.validate(todo);

        assertEquals(1, constraintViolations.size());
        assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void todoIsValid() {
        Todo todo = new Todo( "run");

        Set<ConstraintViolation<Todo>> constraintViolations = validator.validate(todo);

        assertEquals(0, constraintViolations.size());
    }
}
