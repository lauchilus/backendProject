package com.gamelist.main.models.list;

import java.io.IOException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.gamelist.main.igbd.SearchGameListDto;
import com.gamelist.main.models.user.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@CrossOrigin("http://localhost:4200")
@RequestMapping("/api/v1/lists")
@Tag(name = "Lists")
@RequiredArgsConstructor
public class ListController {

	private final ListService listService;
	
	@Operation(summary = "create list", description = "blabla")
	@PostMapping("/{userID}")
	public ResponseEntity<CreateCollectionDto> createList(@PathVariable String userID, @RequestBody @ModelAttribute PostColletionDto post) throws IOException{
		Collection collection = listService.createCollection(userID,post.name(), post.image());
		CreateCollectionDto response = new CreateCollectionDto(collection.getId(), collection.getName(),collection.getLikes(),collection.getImageList().getImageUrl());
		return ResponseEntity.status(HttpStatus.OK).body(response);	
	}
	
	@Operation(summary = "get all lists games from user", description = "blabla")
	@GetMapping("/user/{userID}")
	public ResponseEntity<List<GetCollectionDto>> getLists(@PathVariable String userID, @RequestParam(defaultValue = "0",required = false) int offset, @RequestParam(defaultValue = "12",required = false) int limit){
		List<GetCollectionDto> response = listService.getUserLists(userID,offset,limit);
		return ResponseEntity.status(HttpStatus.OK).body(response);	
	}
	
	@Operation(summary = "add game to a specific collection", description = "blabla")
	@PostMapping("/addGame/{collectionID}")
	public ResponseEntity<String> addCollectionGames(@RequestParam int gameID,@PathVariable String collectionID){
		String response = listService.addGameToCollection(gameID,collectionID);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@Operation(summary = "get all games games from collection", description = "blabla")
	@GetMapping("/{collectionID}")
	public ResponseEntity<List<SearchGameListDto>> getGamesCollection(@PathVariable String collectionID,@RequestParam(defaultValue = "0",required = false) int offset, @RequestParam(defaultValue = "12",required = false) int limit) throws IOException{
		List<SearchGameListDto> response = listService.getGamesCollection(collectionID,offset,limit);
		return ResponseEntity.status(HttpStatus.OK).body(response);
		
	}

	//TODO update likes deletes
	
	
}
