package com.gamelist.main.backlog;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.gamelist.main.igbd.IgdbService;
import com.gamelist.main.igbd.SearchGameListDto;
import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.game.GameRepository;
import com.gamelist.main.models.user.User;
import com.gamelist.main.models.user.UserRepository;

import exceptions.PersonalizedExceptions;
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
	private IgdbService igdb;

	
	
	public BacklogService(BacklogRepository backlogRepo, UserRepository userRepo, GameRepository gameRepo,
			IgdbService igdb) {
		super();
		this.backlogRepo = backlogRepo;
		this.userRepo = userRepo;
		this.gameRepo = gameRepo;
		this.igdb = igdb;
	}

	@Transactional
	public BacklogUserResponseDto addGameToBacklog(String userId, long gameId)
			throws JsonMappingException, JsonProcessingException {
		if(!userRepo.existsById(userId)) {
			throw new PersonalizedExceptions("User not found");
		}
		User user = userRepo.getReferenceById(userId);
		Game game = gameRepo.getReferenceByIgdbGameId(gameId);
		if (game == null) {
			// Create a new Game if it doesn't exist
			game = gameRepo.save(new Game(gameId));

		}

		// Create and save Backlog
		Backlog backlog = backlogRepo.getReferenceByUserAndGame(user, game);
		if(user!= null && user.getBacklog().contains(backlog) && user.getBacklog() != null) {
			throw new RuntimeException("Backlog already exist!!");
		}
		else {
			backlog = backlogRepo.save(new Backlog(user,game)); 
		}
		SearchGameListDto s = igdb.getDataToDto(backlog.getGame().getIgdbGameId());
		BacklogUserResponseDto bs = new BacklogUserResponseDto(backlog.getId(), s.id(),s.name(),s.imageUrl());
 
		return bs;
	}

	public List<BacklogUserResponseDto> getAllBacklogsFromUser(String userId)
			throws JsonMappingException, JsonProcessingException {
		if(!userRepo.existsById(userId)) {
			throw new PersonalizedExceptions("User not found");
		}
		List<Backlog> b = backlogRepo.getReferenceByUserId(userId);
		List<BacklogUserResponseDto> response = new ArrayList<>();
		for (Backlog back : b) {
			SearchGameListDto s = igdb.getDataToDto(back.getGame().getIgdbGameId());
			BacklogUserResponseDto bs = new BacklogUserResponseDto(back.getId(), s.id(),s.name(),s.imageUrl());
			response.add(bs);
		}

		return response;
	}

	// TODO DELETE SERVICE

}
