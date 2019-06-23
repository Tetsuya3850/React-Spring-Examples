package com.example.instagramserver.post;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static com.example.instagramserver.commons.TestConstants.*;
import static org.junit.Assert.assertEquals;

public class PostModelTest {

    private static Validator validator;

    @BeforeClass
    public static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void invalidWhen_ImgPathIsNull() {
        Post post = new Post(POST_ID, null, POST_DESCRIPTION);

        Set<ConstraintViolation<Post>> constraintViolations = validator.validate(post);

        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void valid() {
        Post post = new Post(POST_ID, POST_IMG_PATH, POST_DESCRIPTION);

        Set<ConstraintViolation<Post>> constraintViolations = validator.validate(post);

        assertEquals(0, constraintViolations.size());
    }

}
