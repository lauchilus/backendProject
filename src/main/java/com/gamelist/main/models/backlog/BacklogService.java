package com.gamelist.main.models.backlog;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.gamelist.main.igbd.IgdbService;
import com.gamelist.main.igbd.SearchGameListDto;
import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.game.GameRepository;
import com.gamelist.main.models.user.User;
import com.gamelist.main.models.user.UserRepository;

import com.gamelist.main.exceptions.PersonalizedExceptions;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class BacklogService {


	private final BacklogRepository backlogRepo;


	private final UserRepository userRepo;


	private final GameRepository gameRepo;


	private final IgdbService igdb;



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

	public List<BacklogUserResponseDto> getAllBacklogsFromUser(String userId, Pageable page)
			throws JsonProcessingException {
		if(!userRepo.existsById(userId)) {
			throw new PersonalizedExceptions("User not found");
		}
		List<Backlog> b = backlogRepo.getReferenceByUserId(userId,page);
		List<BacklogUserResponseDto> response = new ArrayList<>();
		for (Backlog back : b) {
			SearchGameListDto s = igdb.getDataToDto(back.getGame().getIgdbGameId());
			BacklogUserResponseDto bs = new BacklogUserResponseDto(back.getId(), s.id(),s.name(),s.imageUrl());
			response.add(bs);
		}

		return response;
	}

	public void delete(long backlogid){
		Backlog backlog = backlogRepo.getReferenceById(backlogid);
		backlogRepo.delete(backlog);
	}
}
