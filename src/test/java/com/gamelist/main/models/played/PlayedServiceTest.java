package com.gamelist.main.models.played;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.print.Pageable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamelist.main.igbd.CoverGame;
import com.gamelist.main.igbd.IgdbService;
import com.gamelist.main.igbd.SearchGameListDto;
import com.gamelist.main.models.favorites.IgdbHelpers;
import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.game.GameRepository;
import com.gamelist.main.models.images.Images;
import com.gamelist.main.models.list.Collection;
import com.gamelist.main.models.list.GameListData;
import com.gamelist.main.models.user.User;
import com.gamelist.main.models.user.UserService;

import jakarta.transaction.Transactional;

@TestPropertySource("classpath:application-test.properties")
@Transactional
@Rollback
class PlayedServiceTest {
	
	@InjectMocks
	private PlayedService playedService;
	
	
	@Mock
	private PlayedRepository playedRepo;
	
	@Mock
	private GameRepository gameRepo;
	
	@Mock
	private UserService userService;
	
	
	@Mock
	private IgdbService igdbService;
	
	@Mock
	private ObjectMapper objectMapper;
	
	@Mock
	private IgdbHelpers igdbHelpers;
	

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAddPlayed() throws Exception {
		User user = User.builder().id("testId").email("test@email.com").username("testUsername")
				.lists(new ArrayList<Collection>()).build();
		Game game = Game.builder().id("testid").igdbGameId(71).build();
		
		Played playedExpected = Played.builder().id("testid").user(user).gameId(game.getIgdbGameId()).build();
		
		when(userService.getUser(anyString())).thenReturn(user);
		when(gameRepo.getReferenceByIgdbGameId(anyLong())).thenReturn(game);
		when(playedRepo.save(any(Played.class))).thenReturn(playedExpected);
		
		Played playedServiceResponse = playedService.addPlayed("testId", 71);
		
		verify(playedRepo,times(1)).save(any());
		
		assertNotNull(playedServiceResponse);
		assertEquals(playedExpected, playedServiceResponse);
		
	}

	@Test
	void testGetAllUserPlayed() throws IOException {
		List<Played> playedList = new ArrayList<>();
		User user = User.builder().id("testId").email("test@email.com").username("testUsername")
				.lists(new ArrayList<Collection>()).build();
		Game game = Game.builder().id("testid").igdbGameId(71).build();
		Played played = Played.builder().id("testid").user(user).gameId(game.getIgdbGameId()).build();
		playedList.add(played);
		
		Images image = Images.builder().id("testid").name("test image").imageUrl("ImageUrl.com").imageId("testID1").build();
		
		
		GameListData[] gldArray = new GameListData[1];
		GameListData gmd = GameListData.builder().id(71).name("Test name").storyline("test Storyine")
				.cover(CoverGame.builder().id(1).image_id("testImageId").build()).follows(0).build();
		gldArray[0] = gmd;
		SearchGameListDto searchGameDto = new SearchGameListDto(gmd.getId(),gmd.getName(),image.getImageUrl());
		PlayedResponse playedResponse = new PlayedResponse(played.getId(),played.getGameId(),played.getFinish_date(),image.getImageUrl());
		String responseIgdbService = "[{\"id\": 71, \"name\": \"Test name\", \"storyline\": \"test's storyline\", \"cover\": {\"id\": 1, \"image_id\": \"testImageId\"}, \"follows\": 0}]";
		
		
		List<PlayedResponse> responseExpected = new ArrayList<>();
		responseExpected.add(playedResponse);
		
		when(playedRepo.findAllByUserIdOrderByIdDesc(anyString(), any())).thenReturn(playedList);
		when(objectMapper.readValue(anyString(), eq(GameListData[].class))).thenReturn(new GameListData[] { gmd });
		when(igdbHelpers.imageBuilder(anyString())).thenReturn("ImageUrl.com");
		when(igdbService.searchGameByIdToList(anyLong())).thenReturn(responseIgdbService);
		
		List<PlayedResponse> responsePlayedService = playedService.getAllUserPlayed("testId", PageRequest.of(0, 9));
		
		assertNotNull(responsePlayedService);
		assertEquals(responseExpected, responsePlayedService);
		assertEquals(responseExpected.size(), responsePlayedService.size());
		
	}

}
