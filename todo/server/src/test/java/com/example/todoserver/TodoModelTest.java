package com.example.todoserver;

import org.junit.BeforeClass;
import org.junit.Test;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static com.example.todoserver.TestConstants.*;
import static org.junit.Assert.assertEquals;

public class TodoModelTest {

    private static Validator validator;

    @BeforeClass
    public static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void invalidWhen_TextIsNull() {
        Todo todo = new Todo( null);

        Set<ConstraintViolation<Todo>> constraintViolations = validator.validate(todo);

        assertEquals(1, constraintViolations.size());
        assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void invalidWhen_TextIsEmptyString() {
        Todo todo = new Todo( "");

        Set<ConstraintViolation<Todo>> constraintViolations = validator.validate(todo);

        assertEquals(1, constraintViolations.size());
        assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void invalidWhen_TextIsOnlySpaces() {
        Todo todo = new Todo( " ");

        Set<ConstraintViolation<Todo>> constraintViolations = validator.validate(todo);

        assertEquals(1, constraintViolations.size());
        assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void valid() {
        Todo todo = new Todo(TODO_TEXT);

        Set<ConstraintViolation<Todo>> constraintViolations = validator.validate(todo);

        assertEquals(0, constraintViolations.size());
    }
}
