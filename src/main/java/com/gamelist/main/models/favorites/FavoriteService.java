package com.gamelist.main.models.favorites;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gamelist.main.igbd.SearchGameListDto;
import com.gamelist.main.models.game.GamesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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

import com.gamelist.main.exceptions.PersonalizedExceptions;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

	private final FavoriteRepository favoriteRepo;

	private final GameRepository gameRepo;

	private final UserRepository userRepo;

	private final IgdbService igdbService;

	private final UserService userService;
	
	private final ObjectMapper objectMapper;
	 
	private final IgdbHelpers igdbHelpers;

	private final GamesService gamesService;

	public List<FavoritesResponseDto> getUserFavorites(String id, int offset, int limit) throws IOException {
		if (!userRepo.existsById(id)) {
			throw new PersonalizedExceptions("User not found.");
		}

		// Utilizar PageRequest para la paginación real en la consulta
		Pageable page = PageRequest.of(offset, limit);
		List<Favorite> favoritesPage = favoriteRepo.findAllByUserId(id,page);

		List<FavoritesResponseDto> responseList = new ArrayList<>();

		// Iterar sobre los elementos de la página actual
		for (Favorite favorite : favoritesPage) {
			SearchGameListDto s = gamesService.getGameList(favorite.getGame().getIgdbGameId());
			FavoritesResponseDto resp = new FavoritesResponseDto(favorite.getId(), favorite.getGame().getIgdbGameId(), s.name(), s.imageUrl());
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
		if(favoriteRepo.countByUserId(userId) >= 5){
			throw new PersonalizedExceptions("You already hav max favorites");
		}
		User user = userService.getUser(userId);
		Game game = gameRepo.getReferenceByIgdbGameId(gameId);
		if(game==null) {
			game = gameRepo.save(new Game(gameId));
			igdbService.getGameDetails(gameId);

		}
		if(favoriteRepo.existsByGameAndUser(game,user)) {
			throw new PersonalizedExceptions("Game already in favorites!");
		}else {
			Favorite fav = favoriteRepo.save(new Favorite(game,user));
			
			user.addFavorite(fav);
			userRepo.save(user);
            return new FavoritesCreateDto(fav.getId(), fav.getGame().getIgdbGameId());
		}
		
		
	}
	
	public List<FavoritesResponseDto> getUserTopFavorites(String id) throws IOException {
		if(!userRepo.existsById(id)) {
			throw new PersonalizedExceptions("user not found.");
		}
		List<Favorite> favorites = favoriteRepo.findTop4ByUserId(id);
		List<FavoritesResponseDto> responseList = new ArrayList<>();
		for (Favorite favorite : favorites) {
			SearchGameListDto s = gamesService.getGameList(favorite.getGame().getIgdbGameId());
			FavoritesResponseDto resp = new FavoritesResponseDto(favorite.getId(), favorite.getGame().getIgdbGameId(), s.name(), s.imageUrl());
			responseList.add(resp);
		}  
		return responseList;
	}

	public GameListData getGamelistDataFromService(String res)
			throws JsonProcessingException, JsonMappingException {
		if(objectMapper != null) {
		GameListData[] data = this.objectMapper.readValue(res, GameListData[].class);
            return data[0];
		}
		else {
			throw new RuntimeException();
		}
	}


	public void deleteFavorite(String favorite) {
		Favorite f = favoriteRepo.getReferenceById(favorite);
		favoriteRepo.delete(f);
	}
}
