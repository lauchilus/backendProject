package com.gamelist.main.models.list;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.igdb.utils.ImageBuilderKt;
import com.api.igdb.utils.ImageSize;
import com.api.igdb.utils.ImageType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamelist.main.cloudinary.CloudinaryComs;
import com.gamelist.main.igbd.GameData;
import com.gamelist.main.igbd.IgdbService;
import com.gamelist.main.igbd.SearchResponseDto;
import com.gamelist.main.models.game.Game;
import com.gamelist.main.models.game.GameRepository;
import com.gamelist.main.models.images.Images;
import com.gamelist.main.models.listGames.ListGames;
import com.gamelist.main.models.listGames.ListGamesRepository;
import com.gamelist.main.models.user.User;
import com.gamelist.main.models.user.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class ListService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private GameRepository gameRepo;

	@Autowired
	private ListGamesRepository listGamesRepo;

	@Autowired
	private CloudinaryComs cloudinary;

	@Autowired
	private ListRepository listRepo;

	@Autowired
	private IgdbService igdbService;

	@Transactional
	public Collection createCollection(long id, String name, MultipartFile image) throws IOException {
		User user = userRepo.getReferenceById(id);
		Collection col;
		if (!image.isEmpty()) {
			Images im = cloudinary.upload(image);
			col = addCollectionToDB(new Collection(name, user, im));
		} else {
			col = addCollectionToDB(new Collection(name, user));
		}
		user.addList(col);
		userRepo.save(user);

		return col;
	}

	private Collection addCollectionToDB(Collection c) {
		Collection col = listRepo.save(c);
		return col;
	}
 
	@Transactional
	public List<GetCollectionDto> getUserLists(long userId) {
		User user = userRepo.getReferenceById(userId);
		List<Collection> list = listRepo.findAllByUser(user);
		List<GetCollectionDto> response = list.stream()
				.map((Collection collection) -> new GetCollectionDto(collection.getId(), collection.getName(),
						collection.getImageList().getImageUrl(), collection.getLikes()))
				.collect(Collectors.toList());
		return response;
	}

	@Transactional
	public String addGameToCollection(long userID, Integer gameID, long collectionID) {
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

	public List<GameResponseDTO> getGamesCollection(long collectionID) throws IOException {
		Collection collection = listRepo.getReferenceById(collectionID);
		List<GameResponseDTO> responseList = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		for (ListGames game : collection.getGamesList()) {
			String res = igdbService.searchGameById2(game.getGame().getIgdbGameId());
			GameListData[] data = objectMapper.readValue(res, GameListData[].class);
			GameListData dat = data[0];
			String image = getImageResponse(dat.getCover());
			GameResponseDTO resp = new GameResponseDTO(dat, image, game.getId());
			responseList.add(resp);
		}
		return responseList;
	} 

	@Transactional
	private void saveGameList(Collection collection, Game game) {
		ListGames listGames = new ListGames(collection, game);
	    listGamesRepo.save(listGames);

	    // Agrega la relación bidireccional 
	    collection.getGamesList().add(listGames);

	    // Guarda las entidades
	    listRepo.save(collection);
	    gameRepo.save(game);

	}

	private SearchResponseDto getGameIgdb(long id) throws IOException {
		String response = igdbService.searchGameById2(id);
		ObjectMapper objectMapper = new ObjectMapper();
		GameData[] dataArray = objectMapper.readValue(response, GameData[].class);
		GameData data = dataArray[0];
		String image = getImageResponse(data.getCover());
		SearchResponseDto resp = new SearchResponseDto(data, image, data.getCollection());
		return resp;
	}

	public String getImageResponse(com.gamelist.main.igbd.CoverGame data) throws IOException {
		String ss = data.getImage_id();
		String imageUrl = ImageBuilderKt.imageBuilder(ss, ImageSize.SCREENSHOT_BIG, ImageType.PNG);
		byte[] image = igdbService.processImage(imageUrl);
		String base64Image = Base64.getEncoder().encodeToString(image);
		return base64Image;
	}

}
