package com.example.todoserver;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TodoServerApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void postFailTest() throws Exception {
        this.mvc.perform(post("/todos").param("text", "")).andExpect(status().isOk());
    }

    @Test
    public void postSuccessTest() throws Exception {
        this.mvc.perform(post("/todos").param("text", "swim")).andExpect(status().isOk());
    }

    @Test
    public void getTest() throws Exception {
        this.mvc.perform(get("/todos")).andExpect(status().isOk());
    }

}
