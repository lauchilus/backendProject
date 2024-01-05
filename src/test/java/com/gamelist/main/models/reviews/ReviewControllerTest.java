package com.gamelist.main.models.reviews;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest
@WebAppConfiguration
@TestPropertySource("classpath:application-test.properties")
@Transactional
@Rollback
class ReviewControllerTest {
	
	private final String BASE_URL = "/reviews";
	
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	

	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void testCreateReviewSuccess() throws JsonProcessingException, Exception {
		ReviewPostDto review = createReviewPost();
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(review))).andReturn();
		
		assertEquals(201, mvcResult.getResponse().getStatus());
	}
	
	@Test
	void testCreateReviewFail() throws JsonProcessingException, Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.content("")).andReturn();
		
		assertEquals(400, mvcResult.getResponse().getStatus());
	}

	@Test
	void testGetReviewSuccess() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/id")
				.queryParam("id", "3")
				).andReturn();
		
		assertEquals(200, mvcResult.getResponse().getStatus());
	}
	
	@Test
	void testGetReviewFail() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/id")
				).andReturn();
		
		assertEquals(400, mvcResult.getResponse().getStatus());
	}

	@Test
	void testGetAllUserReviewsSuccess() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
				.queryParam("userId", "ygLKtqZ05OboHg0woTRyc5kI25G2")
				).andReturn();
		
		assertEquals(200, mvcResult.getResponse().getStatus());
	}
	
	@Test
	void testUserDoesNotHaveReviews_GetAllUserReviewsFail() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
				.queryParam("userId", "fIhZ8yuaGHgDZkZq1Qy6XpwVoRN2")
				).andReturn();
		
		assertEquals(400, mvcResult.getResponse().getStatus());
	}

	@Test
	void testGetTop3UserReviewsSuccess() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/profile")
				.queryParam("userId", "ygLKtqZ05OboHg0woTRyc5kI25G2")
				).andReturn();
		
		assertEquals(200, mvcResult.getResponse().getStatus());
	}
	
	@Test
	void testGetTop3UserReviewsFail() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/profile")
				.queryParam("userId", "error")
				).andReturn();
		
		assertEquals(400, mvcResult.getResponse().getStatus());
	}
	
	private String mapToJson(Object object) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(object);
	}
	
	private ReviewPostDto createReviewPost() {
		return new ReviewPostDto("Review test integration","ygLKtqZ05OboHg0woTRyc5kI25G2",5,420);
	}

}
