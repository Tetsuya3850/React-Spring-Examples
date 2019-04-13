package com.example.blogserver;

import com.example.blogserver.article.Article;
import com.example.blogserver.article.ArticleNotFoundException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.apache.commons.codec.binary.Base64;

import java.util.Map;

import com.example.blogserver.user.SecurityConstants;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BlogServerApplicationTests {

	@Autowired
	private MockMvc mvc;

	String password = "Test3850";
	String text = "Text";
	ObjectMapper mapper = new ObjectMapper();

	public String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Map decodeJWTPayload(String token){
		String base64EncodedBody = token.split("\\.")[1];
		Base64 base64Url = new Base64(true);
		String payload = new String(base64Url.decode(base64EncodedBody));
		try {
			Map<String, Object> jsonPayload = mapper.readValue(payload,
					new TypeReference<Map<String, String>>() {
					});
			return jsonPayload;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public String signupAndSignin(FormUser newUser) throws Exception {
		mvc.perform(post("/users/signup")
				.content(asJsonString(newUser))
				.contentType(MediaType.APPLICATION_JSON)
		);

		MvcResult result = this.mvc.perform(post("/login")
				.content(asJsonString(newUser))
				.contentType(MediaType.APPLICATION_JSON)
		).andReturn();

		return result.getResponse().getContentAsString();
	}

	public Article postArticle(FormArticle newArticle, String token) throws Exception {
		MvcResult result = mvc.perform(post("/articles")
				.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token)
				.content(asJsonString(newArticle))
				.contentType(MediaType.APPLICATION_JSON)
				).andReturn();

		return mapper.readValue(result.getResponse().getContentAsString(), Article.class);
	}

	@Test
	public void testUser() throws Exception {
		String username = "tetsuya@gmail.com";
		FormUser newUser = new FormUser(username, password);


		// Signup success
		mvc.perform(post("/users/signup")
				.content(asJsonString(newUser))
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk());

		// Duplicate email fail
		try {
			mvc.perform(post("/users/signup")
					.content(asJsonString(newUser))
					.contentType(MediaType.APPLICATION_JSON));
		} catch(Exception e){
			assertTrue(e.getCause() instanceof DataIntegrityViolationException);
		}

		// Signin success
		MvcResult result = this.mvc.perform(post("/login")
				.content(asJsonString(newUser))
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk()).andReturn();
		String token = result.getResponse().getContentAsString();
		Map jsonPayload = decodeJWTPayload(token);
		assertEquals(jsonPayload.get("sub"), username);

		// Get User fail without JWT
		String id = String.valueOf(jsonPayload.get("id"));
		mvc.perform(get("/users/{id}", id))
				.andExpect(status().is4xxClientError());

		// Get User success with JWT (no password projection)
		mvc.perform(get("/users/{id}", id).header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value(username))
				.andExpect(jsonPath("$.password").doesNotExist());
	}

	@Test
	public void testArticle() throws Exception {
		String user1 = "user1@gmail.com";
		FormUser newUser = new FormUser(user1, password);
		String token = signupAndSignin(newUser);

		String title1 = "Title 1";
		FormArticle newArticle1 = new FormArticle(title1, text);

		// postArticles success
		mvc.perform(post("/articles")
				.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token)
				.content(asJsonString(newArticle1))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value(title1))
				.andExpect(jsonPath("$.text").value(text))
				.andExpect(jsonPath("$.applicationUser.username").value(user1))
				.andExpect(jsonPath("$.applicationUser.password").doesNotExist());

		String title2 = "Title 2";
		FormArticle newArticle2 = new FormArticle(title2, text);
		postArticle(newArticle2, token);

		String user2 = "user2@gmail.com";
		FormUser newUser2 = new FormUser(user2, password);
		String token2 = signupAndSignin(newUser2);

		String title3 = "Title 3";
		FormArticle newArticle3 = new FormArticle(title3, text);
		postArticle(newArticle3, token2);

		String title4 = "Title 4";
		FormArticle newArticle4 = new FormArticle(title4, text);
		Article article4 = postArticle(newArticle4, token2);

		// Feed first page success
		mvc.perform(get("/articles")
				.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].title").value(title4));

		// Feed next page success
		mvc.perform(get("/articles?page={page}", 1)
				.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].title").value(title1));

		Map jsonPayload = decodeJWTPayload(token);
		String id = String.valueOf(jsonPayload.get("id"));

		// User page success
		mvc.perform(get("/articles/users/{id}", id)
				.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].title").value(title2))
				.andExpect(jsonPath("$[2]").doesNotExist());

		// Get article success
		mvc.perform(get("/articles/{id}", article4.getId())
				.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value(article4.getTitle()));

		String edit_title = "Change";
		FormArticle editArticle = new FormArticle(edit_title, article4.getText());

		// Edit article fail for non-owner
		try {
			mvc.perform(put("/articles/{id}", article4.getId())
					.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token)
					.content(asJsonString(editArticle))
					.contentType(MediaType.APPLICATION_JSON));
		} catch(Exception e){
			assertTrue(e.getCause() instanceof ArticleNotFoundException);
		}

		// Edit article success for owner
		mvc.perform(put("/articles/{id}", article4.getId())
				.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token2)
				.content(asJsonString(editArticle))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value(edit_title));

		// Delete article fail for non-owner
		try {
			mvc.perform(delete("/articles/{id}", article4.getId())
					.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token));
		} catch(Exception e){
			assertTrue(e.getCause() instanceof ArticleNotFoundException);
		}

		// Delete article success
		mvc.perform(delete("/articles/{id}", article4.getId())
				.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token2))
				.andExpect(status().isOk());
		try {
			mvc.perform(get("/articles/{id}", article4.getId())
					.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token));
		} catch(Exception e){
			assertTrue(e.getCause() instanceof ArticleNotFoundException);
		}

	}

}
