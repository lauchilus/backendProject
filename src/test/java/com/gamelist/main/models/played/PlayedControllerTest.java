package com.gamelist.main.models.played;

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
class PlayedControllerTest {

	private final String BASE_URL = "/played";

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void testAddPlayed() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
				.queryParam("user", "ygLKtqZ05OboHg0woTRyc5kI25G2")
				.queryParam("gameID", "964")).andReturn();
		
		assertEquals(200, mvcResult.getResponse().getStatus());
	}

	@Test
	void testGetAllUserPlayed() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/ygLKtqZ05OboHg0woTRyc5kI25G2")
				.queryParam("page", "1")).andReturn();
		
		assertEquals(200, mvcResult.getResponse().getStatus());
	}

}
