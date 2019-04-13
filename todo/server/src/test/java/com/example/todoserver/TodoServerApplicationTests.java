package com.example.todoserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TodoServerApplicationTests {

    @Autowired
    private MockMvc mvc;

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test() throws Exception {
        FormTodo newTodo = new FormTodo();

        this.mvc.perform(post("/todos")
                .content(asJsonString(newTodo))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());

        String todo_text = "swim";

        newTodo.setText(todo_text);

        this.mvc.perform(post("/todos")
                .content(asJsonString(newTodo))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andExpect(jsonPath("$.text").value(todo_text));

        this.mvc.perform(get("/todos")).andExpect(status().isOk()).andExpect(jsonPath("$[0].text").value(todo_text));

        this.mvc.perform(delete("/todos/{id}", 1)).andExpect(status().isOk());

        this.mvc.perform(get("/todos")).andExpect(status().isOk()).andExpect(jsonPath("$[1]").doesNotExist());

    }

}
