package com.gamelist.main.models.favorites;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.gamelist.main.models.user.UserRepository;
import com.gamelist.main.models.user.UserService;

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

	public List<FavoritesResponseDto> getUserFavorites(long id) throws IOException {
		List<Favorite> favorites = favoriteRepo.findAllByUserId(id);
		List<FavoritesResponseDto> responseList = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		for (Favorite favorite : favorites) {
			String res = igdbService.searchGameByIdToList(favorite.getGame().getIgdbGameId());
			GameListData[] data = objectMapper.readValue(res, GameListData[].class);
			GameListData dat = data[0];
			String image = getImageResponse(dat.getCover());
			FavoritesResponseDto resp = new FavoritesResponseDto(favorite.getId(),favorite.getGame().getIgdbGameId(),dat.getName(),image);
			responseList.add(resp);
		}
		return responseList;
	}
	
	public String getImageResponse(CoverGame data) throws IOException {
		String ss = data.getImage_id();
		String imageUrl = ImageBuilderKt.imageBuilder(ss, ImageSize.COVER_BIG, ImageType.PNG);
		
		return imageUrl;
	}

	@Transactional
	public FavoritesCreateDto addFavoriteToUser(long userId, long gameId) {
		User user = userService.getUser(userId);
		Game game = gameRepo.getReferenceByIgdbGameId(gameId);
		if(game==null) {
			game = gameRepo.save(new Game(gameId));
		}
		Favorite fav = favoriteRepo.save(new Favorite(game,user));
		user.addFavorite(fav);
		userRepo.save(user);
		FavoritesCreateDto response = new FavoritesCreateDto(fav.getId(), fav.getGame().getIgdbGameId());
		return response;
	}
	
	
}
