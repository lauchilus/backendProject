package com.gamelist.main.models.list;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
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
class ListControllerTest {
	
	private final String BASE_URL = "/list";
	
	MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;

	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void testCreateList() throws Exception {
		 // Ruta absoluta de la imagen
	    String imagePath = "D:\\webadas\\weebadas\\test-file.jpg";

	    // Crear un objeto File desde la ruta
	    File imageFile = new File(imagePath);

	    // Convertir el archivo a un arreglo de bytes
	    byte[] fileBytes = Files.readAllBytes(imageFile.toPath());

	    // Crear un objeto MockMultipartFile a partir del arreglo de bytes
	    MockMultipartFile image = new MockMultipartFile("image", imageFile.getName(), MediaType.IMAGE_JPEG_VALUE, fileBytes);

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL)
				.file(image)
				.queryParam("userID", "bxCfvy3yDQbVFxt74vSkJ4dMNx92")
				.queryParam("name", "integration")
				).andReturn();		
		assertEquals(200, mvcResult.getResponse().getStatus());
	}

	@Test
	void testGetLists() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
				.queryParam("userID", "bxCfvy3yDQbVFxt74vSkJ4dMNx92")).andReturn();
		
		assertEquals(200, mvcResult.getResponse().getStatus());
	}
	
	@Test
	void testGetListsFail() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
				.queryParam("userID", "bxCfvy3yDQbVFxt74vaaSkJ4dMNx92")).andReturn();
		
		assertEquals(400, mvcResult.getResponse().getStatus());
	}

	@Test
	void testAddCollectionGames() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL+"/addGame")
				.queryParam("gameID", "71")
				.queryParam("collectionID", "4")).andReturn();		
		assertEquals(201, mvcResult.getResponse().getStatus());
	}

	
	@Test
	void testAddCollectionGamesFail() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL+"/addGame")
				.queryParam("gameID", "71")).andReturn();		
		assertEquals(400, mvcResult.getResponse().getStatus());
	}
	
	@Test
	void testGetGamesCollection() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/games")
				.queryParam("collectionID", "4")).andReturn();
		
		assertEquals(200, mvcResult.getResponse().getStatus());
	}
	
	

}
