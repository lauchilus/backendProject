package com.gamelist.main.models.favorites;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import jakarta.transaction.Transactional;

@SpringBootTest
@WebAppConfiguration
@TestPropertySource("classpath:application-test.properties")
@Transactional
@Rollback
class FavoriteControllerTest {

	private final static String BASE_URL = "/favorite";

	MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		
	}

	@Test
	void testGetUserFavoritesSuccess() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
				.queryParam("userId", "U7RNNQ3hsqaDeO08PCOQa1Ms3Cv2")).andReturn();
		
		assertEquals(200, mvcResult.getResponse().getStatus());
	}
	
	@Test
	void testGetUserFavoritesFail() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
				.queryParam("userId", "")).andReturn();
		
		assertEquals(400, mvcResult.getResponse().getStatus());
	}

	@Test
	void testAddFavoriteToUserSuccess() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
				.queryParam("userId", "U7RNNQ3hsqaDeO08PCOQa1Ms3Cv2")
				.queryParam("gameId", "777")).andReturn();
		
		assertEquals(200, mvcResult.getResponse().getStatus());
	}
	
	@Test
	void testAddFavoriteToUserFail() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
				.queryParam("userId", "")
				.queryParam("gameId", "777")).andReturn();
		
		assertEquals(400, mvcResult.getResponse().getStatus());
	}

	@Test
	void testGetTop5FavoritesSuccess() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/profile")
				.queryParam("userId", "U7RNNQ3hsqaDeO08PCOQa1Ms3Cv2")).andReturn();
		
		assertEquals(200, mvcResult.getResponse().getStatus());
	}
	
	@Test
	void testGetTop5FavoritesFail() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/profile")
				.queryParam("userId", "")
				.queryParam("gameId", "777")).andReturn();
		
		assertEquals(400, mvcResult.getResponse().getStatus());
	}

}
