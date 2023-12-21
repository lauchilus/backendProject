package com.gamelist.main.backlog;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.gamelist.main.igbd.IgdbService;
import com.gamelist.main.igbd.SearchGameListDto;
import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.game.GameRepository;
import com.gamelist.main.models.user.User;
import com.gamelist.main.models.user.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class BacklogService {

	@Autowired
	private BacklogRepository backlogRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private GameRepository gameRepo;
	
	@Autowired
	private IgdbService igdb;;
	
	@Transactional
	public Backlog addGameToBacklog(long userId, long gameId) {
		User user = userRepo.getReferenceById(userId);
	    Game game = gameRepo.getReferenceByIgdbGameId(gameId);
	    System.out.println(game);
	    if (game == null) {
	        // Create a new Game if it doesn't exist
	        game = gameRepo.save(new Game(gameId));
	    }

	    // Create and save Backlog
	    Backlog backlog = backlogRepo.save(new Backlog(user, game));

	    return backlog;
	}
	
	
	public BacklogUserResponseDto getAllBacklogsFromUser(long userId) throws JsonMappingException, JsonProcessingException {
		List<Backlog> b = backlogRepo.getReferenceByUserId(userId);
		List<SearchGameListDto> search = new ArrayList<>();
		for (Backlog back : b) {
			SearchGameListDto s = igdb.getDataToDto(back.getGame().getIgdbGameId());
			search.add(s);
		}
		BacklogUserResponseDto response = new BacklogUserResponseDto(search);
		return response;
	}

}
