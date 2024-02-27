package com.gamelist.main.models.list;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.igdb.utils.ImageBuilderKt;
import com.api.igdb.utils.ImageSize;
import com.api.igdb.utils.ImageType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamelist.main.cloudinary.CloudinaryComs;
import com.gamelist.main.igbd.GameData;
import com.gamelist.main.igbd.IgdbService;
import com.gamelist.main.igbd.SearchGameListDto;
import com.gamelist.main.igbd.SearchResponseDto;
import com.gamelist.main.models.favorites.IgdbHelpers;
import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.game.GameRepository;
import com.gamelist.main.models.images.Images;
import com.gamelist.main.models.listGames.ListGames;
import com.gamelist.main.models.listGames.ListGamesRepository;
import com.gamelist.main.models.user.User;
import com.gamelist.main.models.user.UserRepository;

import exceptions.PersonalizedExceptions;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ListService {

	private final UserRepository userRepo;

	private final GameRepository gameRepo;

	private final ListGamesRepository listGamesRepo;

	private final CloudinaryComs cloudinary;

	private final ListRepository listRepo;

	private final IgdbService igdbService;
	
	private final IgdbHelpers igdbHelpers;
	
	private final ObjectMapper objectMapper;


	@Transactional
	public Collection createCollection(String id, String name, MultipartFile image) throws IOException {
		if(!userRepo.existsById(id)) {
			throw new PersonalizedExceptions(id);
		}
		
		User user = userRepo.getReferenceById(id);
		Collection col;
		if (image != null) {
			Images im = cloudinary.upload(image);
			col = addCollectionToDB(new Collection(name, user, im));
		} else {
			col = addCollectionToDB(new Collection(name, user));
		}
		user.addList(col);
		userRepo.save(user);

		return col;
	}

	public Collection addCollectionToDB(Collection c) {
		Collection col = listRepo.save(c);
		return col;
	}
 
	@Transactional
	public List<GetCollectionDto> getUserLists(String userId,int offset,int limit) {
		if(!userRepo.existsById(userId)) {
			throw new PersonalizedExceptions(userId);
		}
		Pageable page = PageRequest.of(offset,limit);
		User user = userRepo.getReferenceById(userId);
		List<Collection> list = listRepo.findAllByUser(user,page);
		if(list.isEmpty() || list == null) {
			throw new PersonalizedExceptions("No lists");
		}
		List<GetCollectionDto> response = list.stream()
				.map((Collection collection) -> new GetCollectionDto(collection.getId(), collection.getName(),collection.getImageList().getImageUrl(), collection.getLikes()))
				.collect(Collectors.toList());
		return response;
	}

	@Transactional
	public String addGameToCollection(Integer gameID, String collectionID) {
		Collection collection = listRepo.getReferenceById(collectionID);
		Game game = gameRepo.getReferenceByIgdbGameId(gameID);
		if (game == null) {
			game = gameRepo.save(new Game(gameID));
			saveGameList(collection, game);
		}else {
			saveGameList(collection, game);
		}

		
		return "Game Added";
	}

	public List<SearchGameListDto> getGamesCollection(String collectionID,int offset, int limit) throws IOException {
		Collection collection = listRepo.getReferenceById(collectionID);
		List<ListGames> listGames = collection.getGamesList();
		List<SearchGameListDto> responseList = new ArrayList<>();
		for (ListGames game : listGames) {
			String res = igdbService.searchGameByIdToList(game.getGame().getIgdbGameId());
			GameListData dat = getGamelistDataFromService(res);
			String image = getImageResponse(dat.getCover());
			SearchGameListDto resp = new SearchGameListDto(dat.getId(),dat.getName(),image);
			responseList.add(resp);
		}
		return responseList;
	} 

	@Transactional
	private void saveGameList(Collection collection, Game game) {
		ListGames listGames = new ListGames(collection, game);
	    listGamesRepo.save(listGames);
	    collection.getGamesList().add(listGames);
	    listRepo.save(collection);
	    gameRepo.save(game);

	}

	private SearchResponseDto getGameIgdb(long id) throws IOException {
		String response = igdbService.searchGameByIdToList(id);
		ObjectMapper objectMapper = new ObjectMapper();
		GameData[] dataArray = objectMapper.readValue(response, GameData[].class);
		GameData data = dataArray[0];
		String image = getImageResponse(data.getCover());
		SearchResponseDto resp = new SearchResponseDto(data, image, data.getCollection());
		return resp;
	}

	public String getImageResponse(com.gamelist.main.igbd.CoverGame data){
		String ss = data.getImage_id();
		String imageUrl = igdbHelpers.imageBuilder(ss);
		return imageUrl;
	}
	
	public GameListData getGamelistDataFromService(String res)
			throws JsonProcessingException {
		if(objectMapper != null) {
		System.out.println(res+"oooo************");
		GameListData[] data = this.objectMapper.readValue(res, GameListData[].class);
		GameListData dat = data[0];
		return dat;
		}
		else {
			throw new PersonalizedExceptions("Something went wrong");
		}
	}

}
