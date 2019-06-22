package com.example.blogserver.article;

import com.example.blogserver.commons.TestUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static com.example.blogserver.commons.TestConstants.*;

public class ArticleModelTest {

    private static Validator validator;

    @BeforeClass
    public static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void invalidWhen_TitleIsNull() {
        Article article = new Article(null, ARTICLE_TEXT);

        Set<ConstraintViolation<Article>> constraintViolations = validator.validate(article);

        assertEquals(1, constraintViolations.size());
        assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void invalidWhen_TitleIsEmptyString() {
        Article article = new Article("", ARTICLE_TEXT);

        Set<ConstraintViolation<Article>> constraintViolations = validator.validate(article);

        assertEquals(1, constraintViolations.size());
        assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void invalidWhen_TitleIsOnlySpaces() {
        Article article = new Article(" ", ARTICLE_TEXT);

        Set<ConstraintViolation<Article>> constraintViolations = validator.validate(article);

        assertEquals(1, constraintViolations.size());
        assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void invalidWhen_TitleIsLongerThan100() {
        Article article = new Article(TestUtils.dummyStringWithSpecifiedLength(101), ARTICLE_TEXT);

        Set<ConstraintViolation<Article>> constraintViolations = validator.validate(article);

        assertEquals(1, constraintViolations.size());
        assertEquals("size must be between 0 and 100", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void invalidWhen_TextIsNull() {
        Article article = new Article(ARTICLE_TITLE, null);

        Set<ConstraintViolation<Article>> constraintViolations = validator.validate(article);

        assertEquals(1, constraintViolations.size());
        assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void invalidWhen_TextIsEmptyString() {
        Article article = new Article(ARTICLE_TITLE, "");

        Set<ConstraintViolation<Article>> constraintViolations = validator.validate(article);

        assertEquals(1, constraintViolations.size());
        assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void invalidWhen_TextIsOnlySpaces() {
        Article article = new Article(ARTICLE_TITLE, " ");

        Set<ConstraintViolation<Article>> constraintViolations = validator.validate(article);

        assertEquals(1, constraintViolations.size());
        assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void invalidWhen_TextIsLongerThan10000() {
        Article article = new Article(ARTICLE_TITLE, TestUtils.dummyStringWithSpecifiedLength(10001));

        Set<ConstraintViolation<Article>> constraintViolations = validator.validate(article);

        assertEquals(1, constraintViolations.size());
        assertEquals("size must be between 0 and 10000", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void valid() {
        Article article = new Article(ARTICLE_TITLE, ARTICLE_TEXT);

        Set<ConstraintViolation<Article>> constraintViolations = validator.validate(article);

        assertEquals(0, constraintViolations.size());
    }
}
