package com.gamelist.main.models.played;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.igdb.utils.ImageBuilderKt;
import com.api.igdb.utils.ImageSize;
import com.api.igdb.utils.ImageType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamelist.main.igbd.CoverGame;
import com.gamelist.main.igbd.IgdbService;
import com.gamelist.main.models.favorites.IgdbHelpers;
import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.game.GameRepository;
import com.gamelist.main.models.list.GameListData;
import com.gamelist.main.models.user.User;
import com.gamelist.main.models.user.UserRepository;
import com.gamelist.main.models.user.UserService;

import jakarta.transaction.Transactional;

@Service
public class PlayedService {

	@Autowired
	private PlayedRepository playedRepo;
	
	@Autowired
	private GameRepository gameRepo;
	
	@Autowired
	private UserService userService;
	
	
	@Autowired
	private IgdbService igdbService;
	
	private ObjectMapper objectMapper;
	
	private IgdbHelpers igdbHelpers;
	
	
	 
	public PlayedService(PlayedRepository playedRepo, GameRepository gameRepo, UserService userService,
			IgdbService igdbService, ObjectMapper objectMapper, IgdbHelpers igdbHelpers) {
		super();
		this.playedRepo = playedRepo;
		this.gameRepo = gameRepo;
		this.userService = userService;
		this.igdbService = igdbService;
		this.objectMapper = objectMapper;
		this.igdbHelpers = igdbHelpers;
	}


	@Transactional
	public Played addPlayed(String id, long gameId) throws Exception {
		User user = userService.getUser(id);
		Game game = gameRepo.getReferenceByIgdbGameId(gameId);
		if(game==null) {
			game = gameRepo.save(new Game(gameId));
		}
		if(playedRepo.existsByGameIdAndUser(game.getIgdbGameId(),user)) {
			throw new Exception("Game already in list!");
		}
		
		Played played = playedRepo.save(new Played(user,game.getIgdbGameId()));
		return played;
	}


	public List<PlayedResponse> getAllUserPlayed(String userid,Pageable page) throws IOException {
		List<Played> playedList = playedRepo.findAllByUserIdOrderByIdDesc(userid,page);
		List<PlayedResponse> responseList = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		for (Played p : playedList) {
			String res = igdbService.searchGameByIdToList(p.getGameId());
			GameListData dat = getGamelistDataFromService(res);
			String image = getImageResponse(dat.getCover());
			PlayedResponse resp = new PlayedResponse(p.getId(),p.getGameId(),p.getFinish_date(),image);
			responseList.add(resp);
		}
		return responseList;
	}
	
	
	public String getImageResponse(CoverGame data) throws IOException {
		String ss = data.getImage_id();
		String imageUrl = igdbHelpers.imageBuilder(ss);
		
		return imageUrl;
	}
	
	
	public GameListData getGamelistDataFromService(String res)
			throws JsonProcessingException, JsonMappingException {
		if(objectMapper != null) {
		System.out.println(res+"oooo************");
		GameListData[] data = this.objectMapper.readValue(res, GameListData[].class);
		GameListData dat = data[0];
		return dat;
		}
		else {
			throw new RuntimeException();
		}
	}
	
	
}
