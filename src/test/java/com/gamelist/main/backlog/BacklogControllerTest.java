package com.gamelist.main.backlog;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
class BacklogControllerTest {

	private final static String BASE_URL = "/backlog";
	
	
	MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
	}
	
	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		
	}

	@Test
	void getAllBacklogFromUserSuccess() throws Exception {
		MvcResult mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
				.queryParam("userId", "bxCfvy3yDQbVFxt74vSkJ4dMNx92")
				.accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		System.out.println(mockMvcResult.getResponse().getContentAsString());
		assertEquals(200, mockMvcResult.getResponse().getStatus());
				
	}
	
	@Test
	void getAllBacklogFromUserFail() throws Exception {
		MvcResult mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
				.queryParam("userId", "")
				.accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		System.out.println(mockMvcResult.getResponse().getContentAsString());
		assertEquals(400, mockMvcResult.getResponse().getStatus());
				
	}
	
	@Test
	 void addGameToBacklogSuccess() throws Exception {
		MvcResult mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
				.queryParam("user", "bxCfvy3yDQbVFxt74vSkJ4dMNx92")
				.queryParam("gameId", "777" )
				.accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		
		assertEquals(201, mockMvcResult.getResponse().getStatus());
		
	}
	
	@Test
	void addGameToBacklogFail() throws Exception {
		MvcResult mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
				.queryParam("user", "")
				.queryParam("gameId", "71" )
				.accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		
		assertEquals(400, mockMvcResult.getResponse().getStatus());
	}

}
