package com.gamelist.main.models.favorites;

import java.io.IOException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@CrossOrigin("*")
@RequestMapping("/favorite")
@Tag(name="Favorite")
@RequiredArgsConstructor
public class FavoriteController {


	private final FavoriteService favoriteService;

	@Operation(summary = "get all favorites games from user", description = "blabla")
	@GetMapping
	public ResponseEntity<List<FavoritesResponseDto>> getUserFavorites(@RequestParam String userId) throws IOException{
		List<FavoritesResponseDto> response  = favoriteService.getUserFavorites(userId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@Operation(summary = "add a favorite", description = "blabla")
	@PostMapping
	public ResponseEntity<FavoritesCreateDto> addFavoriteToUser(@RequestParam String userId,@RequestParam long gameId) throws Exception{
		FavoritesCreateDto response = favoriteService.addFavoriteToUser(userId,gameId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@Operation(summary = "get top 5 favorites games from user", description = "blabla")
	@GetMapping("/profile")
	public ResponseEntity<List<FavoritesResponseDto>> getTop5Favorites(@RequestParam String userId) throws IOException{
		List<FavoritesResponseDto> response  = favoriteService.getUserTopFavorites(userId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	//TODO DELETE
	
	
}
