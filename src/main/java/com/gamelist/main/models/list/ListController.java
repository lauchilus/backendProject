package com.gamelist.main.models.list;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.gamelist.main.igbd.SearchGameListDto;
import com.gamelist.main.models.user.UserRepository;

@Controller
@CrossOrigin
@RequestMapping("/list")
public class ListController {

	@Autowired 
	private ListService listService;
	
	@PostMapping
	public ResponseEntity<CreateCollectionDto> createList(@RequestParam long userID, @RequestParam String name, MultipartFile image) throws IOException{
		Collection collection = listService.createCollection(userID,name,image);
		CreateCollectionDto response = new CreateCollectionDto(collection.getName(),collection.getLikes(),collection.getImageList().getImageUrl());
		return ResponseEntity.ok(response);		
	}
	
	@GetMapping
	public ResponseEntity<List<GetCollectionDto>> getLists(@RequestParam long userID){
		List<GetCollectionDto> response = listService.getUserLists(userID);
		return ResponseEntity.ok(response);		
	}
	
	@PostMapping("/addGame")
	public ResponseEntity<String> addCollectionGames(@RequestParam long userID,@RequestParam int gameID,@RequestParam long collectionID){
		String response = listService.addGameToCollection(userID,gameID,collectionID);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/games")
	public ResponseEntity<List<SearchGameListDto>> getGamesCollection(@RequestParam long collectionID) throws IOException{
		List<SearchGameListDto> response = listService.getGamesCollection(collectionID);
		return ResponseEntity.ok(response);
		
	}
	
	
}
