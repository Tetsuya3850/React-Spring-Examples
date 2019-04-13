package com.example.authserver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.apache.commons.codec.binary.Base64;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthServerApplicationTests {

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

	public static Map decodeJWTPayload(String token){
		String base64EncodedBody = token.split("\\.")[1];
		Base64 base64Url = new Base64(true);
		String payload = new String(base64Url.decode(base64EncodedBody));
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonPayload;
		try {
			jsonPayload = mapper.readValue(payload,
					new TypeReference<Map<String, String>>() {
					});
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return jsonPayload;
	}

	@Test
	public void test() throws Exception {
		String username = "tetsuya@gmail.com";
		String password = "Test3850";
		FormUser newUser = new FormUser();
		newUser.setUsername(username);
		newUser.setPassword(password);

		mvc.perform(post("/users/signup")
				.content(asJsonString(newUser))
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk());

		MvcResult result = this.mvc.perform(post("/login")
				.content(asJsonString(newUser))
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk()).andReturn();

		String token = result.getResponse().getContentAsString();
		Map jsonPayload = decodeJWTPayload(token);

		assertEquals(jsonPayload.get("sub"), username);

		String id = String.valueOf(jsonPayload.get("id"));

		mvc.perform(get("/users/{id}", id))
				.andExpect(status().is4xxClientError());

		mvc.perform(get("/users/{id}", id).header(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value(username))
				.andExpect(jsonPath("$.password").doesNotExist());

	}

}
