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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamelist.main.igbd.CoverGame;
import com.gamelist.main.igbd.IgdbService;
import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.game.GameRepository;
import com.gamelist.main.models.list.GameListData;
import com.gamelist.main.models.user.User;
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


	public List<PlayedResponse> getAllUserPlayed(long userid,Pageable page) throws IOException {
		List<Played> playedList = playedRepo.findAllByUserIdOrderByIdDesc(userid,page);
		List<PlayedResponse> responseList = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		for (Played p : playedList) {
			String res = igdbService.searchGameByIdToList(p.getGameId());
			GameListData[] data = objectMapper.readValue(res, GameListData[].class);
			GameListData dat = data[0];
			String image = getImageResponse(dat.getCover());
			PlayedResponse resp = new PlayedResponse(p.getId(),p.getGameId(),p.getFinish_date(),image);
			responseList.add(resp);
		}
		return responseList;
	}
	
	public String getImageResponse(CoverGame data) throws IOException {
		String ss = data.getImage_id();
		String imageUrl = ImageBuilderKt.imageBuilder(ss, ImageSize.COVER_BIG, ImageType.PNG);
		
		return imageUrl;
	}
	
	
	
}
