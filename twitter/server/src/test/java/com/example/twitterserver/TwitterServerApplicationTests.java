package com.example.twitterserver;

import com.example.twitterserver.tweet.Tweet;
import com.example.twitterserver.tweet.TweetNotFoundException;
import com.example.twitterserver.user.SecurityConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
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

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TwitterServerApplicationTests {

	@Autowired
	private MockMvc mvc;

	String password = "Test3850";
	String text = "Hello World!";
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

	public String getIdFromJWT(String token){
		Map jsonPayload = decodeJWTPayload(token);
		return String.valueOf(jsonPayload.get("id"));
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

	public Tweet postTweet(FormTweet newTweet, String token) throws Exception {
		MvcResult result = mvc.perform(post("/tweets")
				.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token)
				.content(asJsonString(newTweet))
				.contentType(MediaType.APPLICATION_JSON)
		).andReturn();

		return mapper.readValue(result.getResponse().getContentAsString(), Tweet.class);
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
	public void testTweet() throws Exception {
		String user1 = "user1@gmail.com";
		FormUser newUser = new FormUser(user1, password);
		String token1 = signupAndSignin(newUser);

		String text1 = "Text 1";
		FormTweet newTweet1 = new FormTweet(text1);

		// postTweet
		mvc.perform(post("/tweets")
				.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token1)
				.content(asJsonString(newTweet1))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.text").value(text1))
				.andExpect(jsonPath("$.applicationUser.username").value(user1))
				.andExpect(jsonPath("$.applicationUser.password").doesNotExist());

		String user2 = "user2@gmail.com";
		FormUser newUser2 = new FormUser(user2, password);
		String token2 = signupAndSignin(newUser2);

		String text2 = "Text 2";
		FormTweet newTweet2 = new FormTweet(text2);
		postTweet(newTweet2, token2);

		String user3 = "user3@gmail.com";
		FormUser newUser3 = new FormUser(user3, password);
		String token3 = signupAndSignin(newUser3);

		String text3 = "Text 3";
		FormTweet newTweet3 = new FormTweet(text3);
		Tweet tweet3 = postTweet(newTweet3, token3);

		// Feed only own tweet
		mvc.perform(get("/tweets")
				.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].text").value(text1))
				.andExpect(jsonPath("$[1]").doesNotExist());

		String id1 = getIdFromJWT(token1);
		String id2 = getIdFromJWT(token2);
		System.out.println(id2);

		// user1 follows user2
		mvc.perform(post("/follows/{id}", id2)
				.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token1))
				.andExpect(status().isOk());

		// user1's follow count
		mvc.perform(get("/users/{id}", id1)
				.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.followingCount").value(1))
				.andExpect(jsonPath("$.followersCount").value(0));

		// user2's follow count
		mvc.perform(get("/users/{id}", id2)
				.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.followingCount").value(0))
				.andExpect(jsonPath("$.followersCount").value(1));

		// Feed with user2 tweet
		mvc.perform(get("/tweets")
				.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].text").value(text2))
				.andExpect(jsonPath("$[1].text").value(text1))
				.andExpect(jsonPath("$[2]").doesNotExist());

		// user1 unfollows user2
		mvc.perform(post("/follows/{id}", id2)
				.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token1))
				.andExpect(status().isOk());

		// Feed only own tweet
		mvc.perform(get("/tweets")
				.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].text").value(text1))
				.andExpect(jsonPath("$[1]").doesNotExist());

		// User page
		mvc.perform(get("/tweets/users/{userId}", id1)
				.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].text").value(text1))
				.andExpect(jsonPath("$[1]").doesNotExist());

		// Get tweet
		mvc.perform(get("/tweets/{tweetId}", tweet3.getId())
				.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.text").value(tweet3.getText()));

		// Heart tweet
		mvc.perform(post("/hearts/{tweetId}", tweet3.getId())
				.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token1))
				.andExpect(status().isOk());

		// Heart count incremented
		mvc.perform(get("/tweets/{tweetId}", tweet3.getId())
				.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.heartCount").value(1));

		// UnHeart tweet
		mvc.perform(post("/hearts/{tweetId}", tweet3.getId())
				.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token1))
				.andExpect(status().isOk());

		// Heart count decremented
		mvc.perform(get("/tweets/{tweetId}", tweet3.getId())
				.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.heartCount").value(0));

		// Delete tweet fail for non-owner
		try {
			mvc.perform(delete("/tweets/{id}", tweet3.getId())
					.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token1));
		} catch(Exception e){
			assertTrue(e.getCause() instanceof TweetNotFoundException);
		}

		// Delete tweet
		mvc.perform(delete("/tweets/{id}", tweet3.getId())
				.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token3))
				.andExpect(status().isOk());
		try {
			mvc.perform(get("/tweets/{id}", tweet3.getId())
					.header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token3));
		} catch(Exception e){
			assertTrue(e.getCause() instanceof TweetNotFoundException);
		}

	}

}
