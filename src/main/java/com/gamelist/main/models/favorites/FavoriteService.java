package com.gamelist.main.models.favorites;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.igdb.utils.ImageBuilderKt;
import com.api.igdb.utils.ImageSize;
import com.api.igdb.utils.ImageType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamelist.main.igbd.CoverGame;
import com.gamelist.main.igbd.IgdbService;
import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.game.GameRepository;
import com.gamelist.main.models.list.GameListData;
import com.gamelist.main.models.user.User;
import com.gamelist.main.models.user.UserRepository;
import com.gamelist.main.models.user.UserService;

import exceptions.PersonalizedExceptions;
import jakarta.transaction.Transactional;

@Service
public class FavoriteService {
	
	
	
	@Autowired
	private FavoriteRepository favoriteRepo;
	
	@Autowired
	private GameRepository gameRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private IgdbService igdbService;
	
	@Autowired
	private UserService userService;
	
	private ObjectMapper objectMapper;
	 
	private IgdbHelpers igdbHelpers;

	
	
	public FavoriteService(FavoriteRepository favoriteRepo, GameRepository gameRepo, UserRepository userRepo,
			IgdbService igdbService, UserService userService,ObjectMapper objectMapper,IgdbHelpers igdbHelpers) {
		super();
		this.favoriteRepo = favoriteRepo;
		this.gameRepo = gameRepo;
		this.userRepo = userRepo;
		this.igdbService = igdbService;
		this.userService = userService;
		this.objectMapper = objectMapper;
		this.igdbHelpers = igdbHelpers;
	} 

	public List<FavoritesResponseDto> getUserFavorites(String id) throws IOException {
		if(!userRepo.existsById(id)) {
			throw new PersonalizedExceptions("user not found.");
		}
		List<Favorite> favorites = favoriteRepo.findAllByUserId(id);
		List<FavoritesResponseDto> responseList = new ArrayList<>();
		for (Favorite favorite : favorites) {
			String res = igdbService.searchGameByIdToList(favorite.getGame().getIgdbGameId());
			GameListData dat = getGamelistDataFromService(res);
			String image = getImageResponse(dat.getCover());
			FavoritesResponseDto resp = new FavoritesResponseDto(favorite.getId(),favorite.getGame().getIgdbGameId(),dat.getName(),image);
			responseList.add(resp);
		}
		return responseList;
	}
	
	public String getImageResponse(CoverGame data) throws IOException {
		String ss = data.getImage_id();
		String imageUrl = imageBuilder(ss);
		
		return imageUrl;
	}

	public String imageBuilder(String ss) {
		return igdbHelpers.imageBuilder(ss);
	}

	@Transactional
	public FavoritesCreateDto addFavoriteToUser(String userId, long gameId) throws Exception {
		if(!userRepo.existsById(userId)) {
			throw new PersonalizedExceptions("user not found.");
		}
		User user = userService.getUser(userId);
		Game game = gameRepo.getReferenceByIgdbGameId(gameId);
		if(game==null) {
			game = gameRepo.save(new Game(gameId));
		}
		if(favoriteRepo.existsByGameAndUser(game,user)) {
			throw new PersonalizedExceptions("Game already in favorites!");
		}else {
			Favorite fav = favoriteRepo.save(new Favorite(game,user));
			
			user.addFavorite(fav);
			userRepo.save(user);
			FavoritesCreateDto response = new FavoritesCreateDto(fav.getId(), fav.getGame().getIgdbGameId());
			return response;
		}
		
		
	}
	
	public List<FavoritesResponseDto> getUserTopFavorites(String id) throws IOException {
		if(!userRepo.existsById(id)) {
			throw new PersonalizedExceptions("user not found.");
		}
		List<Favorite> favorites = favoriteRepo.findTop4ByUserId(id);
		List<FavoritesResponseDto> responseList = new ArrayList<>();
		for (Favorite favorite : favorites) {
			String res = igdbService.searchGameByIdToList(favorite.getGame().getIgdbGameId());
			System.out.println(res);
			GameListData dat = getGamelistDataFromService(res);
			String image = getImageResponse(dat.getCover());
			FavoritesResponseDto resp = new FavoritesResponseDto(favorite.getId(),
					favorite.getGame().getIgdbGameId(),dat.getName(),image);
			responseList.add(resp);
		}  
		return responseList;
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
