package com.gamelist.main.backlog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.gamelist.main.models.backlog.Backlog;
import com.gamelist.main.models.backlog.BacklogRepository;
import com.gamelist.main.models.backlog.BacklogService;
import com.gamelist.main.models.backlog.BacklogUserResponseDto;
import com.gamelist.main.models.game.GamesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.gamelist.main.igbd.IgdbService;
import com.gamelist.main.igbd.SearchGameListDto;
import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.game.GameRepository;
import com.gamelist.main.models.user.User;
import com.gamelist.main.models.user.UserRepository;

import jakarta.transaction.Transactional;

@TestPropertySource("classpath:application-test.properties")
@Transactional
@Rollback
public class BacklogServiceTest {

	@InjectMocks
	private BacklogService backlogService;
	
	@Mock
	private BacklogRepository backlogRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private GameRepository gameRepository;

	@Mock
	private IgdbService igdb;

	@Mock
	private GamesService gamesService;

	@BeforeEach
	void setUp() {
		
		MockitoAnnotations.openMocks(this);
		backlogService = new BacklogService(backlogRepository,userRepository,gameRepository,igdb,gamesService);
		
	}
	
	@Test
	public void shouldReturnBacklogResponseDto() throws JsonMappingException, JsonProcessingException {
		User user = User.builder().id("testId").email("test@email.com").username("testUsername").backlog(new ArrayList<Backlog>()).build();
		Game game = Game.builder().id("testId").igdbGameId(71).build();
		SearchGameListDto searchGameMock = new SearchGameListDto(71, "test Game" , "imageUrl.com");
		Backlog backlogMock = Backlog.builder().id(1).user(user).game(game).build();
		BacklogUserResponseDto responseExpected = new BacklogUserResponseDto(1,71,"test Game" , "imageUrl.com");
		
		
		when(userRepository.getReferenceById("testId")).thenReturn(user);
		when(gameRepository.getReferenceById(any())).thenReturn(game);
		when(backlogRepository.getReferenceByUserAndGame(user, game)).thenReturn(backlogMock);
		when(igdb.getDataToDto(game.getIgdbGameId())).thenReturn(searchGameMock);
		when(backlogRepository.save(any())).thenReturn(backlogMock);
		when(userRepository.existsById(anyString())).thenReturn(true);
		BacklogUserResponseDto backlog = backlogService.addGameToBacklog("first", 71);
		 
		verify(backlogRepository,times(1)).save(any(Backlog.class));
		
		assertNotNull(backlog);
		assertEquals(backlog, responseExpected);
	}
	
	@Test
	public void shouldReturnAllBacklogsFromUser() throws JsonMappingException, JsonProcessingException {
		List<Backlog> backlogs = new ArrayList<>();
		User user = User.builder().id("testId").email("test@email.com").username("testUsername").backlog(new ArrayList<Backlog>()).build();
		Game game = Game.builder().id("testId").igdbGameId(71).build();
		backlogs.add(Backlog.builder().id(1).user(user).game(game).build());
		SearchGameListDto searchGameMock = new SearchGameListDto(71, "test Game" , "imageUrl.com");
		
		when(backlogRepository.getReferenceByUserId(user.getId(),any())).thenReturn(backlogs);
		when(igdb.getDataToDto(game.getIgdbGameId())).thenReturn(searchGameMock);
		when(userRepository.existsById(anyString())).thenReturn(true);
		Pageable page = PageRequest.of(0,5);
		List<BacklogUserResponseDto> response = backlogService.getAllBacklogsFromUser("testId",page);
		
		verify(backlogRepository,times(1)).getReferenceByUserId(user.getId(),any());
		
		assertNotNull(response);
		assertEquals(backlogs.size(), response.size());
		 
	} 
	

}
