package com.gamelist.main.backlog;

import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.gamelist.main.igbd.SearchGameListDto;
import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.game.GameRepository;
import com.gamelist.main.models.user.User;
import com.gamelist.main.models.user.UserRepository;

@ExtendWith(MockitoExtension.class)
public class BacklogServiceTest {

	@Mock
	private BacklogRepository backlogRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private GameRepository gameRepository;
	
	@InjectMocks
	private BacklogService backlogService;
	
	@Test
	public void backlogService_addGameToBacklog_ReturnBacklog() throws JsonMappingException, JsonProcessingException {
		User user = User.builder().id(1l).username("testUser").build();
		Game game  = Game.builder().id(1).igdbGameId(733).build();
		Backlog backlog = Backlog.builder().user(user).game(game).build();
		BacklogUserResponseDto response = BacklogUserResponseDto.builder().idBacklog(1).game(new SearchGameListDto(1,"testing game","testingUrl.com")).build();
		
		
		when(userRepository.getReferenceById(Mockito.any(Long.class))).thenReturn(user);
		when(gameRepository.getReferenceByIgdbGameId(Mockito.any(Long.class))).thenReturn(game);
		when(backlogRepository.save(Mockito.any(Backlog.class))).thenReturn(backlog);
		//when(backlogService.addGameToBacklog(Mockito.any(Long.class), Mockito.any(Long.class))).thenReturn(response);
		BacklogUserResponseDto savedBacklog = backlogService.addGameToBacklog(1, 724);
		
		Assertions.assertThat(savedBacklog).isNotNull();		
	}
	
}
