package com.example.instagramserver.tag;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static com.example.instagramserver.commons.TestConstants.*;
import static org.junit.Assert.assertEquals;

@DataJpaTest
@RunWith(SpringRunner.class)
public class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void findByText_Success(){
        testEntityManager.persistAndFlush(new Tag(TAG_TEXT_1));
        Tag tag = tagRepository.findByText(TAG_TEXT_1);
        assertEquals(TAG_TEXT_1, tag.getText());
    }

}
