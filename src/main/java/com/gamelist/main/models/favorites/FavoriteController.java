package com.gamelist.main.models.favorites;

import java.io.IOException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@CrossOrigin("*")
@RequestMapping("/favorites")
@Tag(name="Favorite")
@RequiredArgsConstructor
public class FavoriteController {


	private final FavoriteService favoriteService;

	@Operation(summary = "get all favorites games from user", description = "blabla")
	@GetMapping("/{userId}")
	public ResponseEntity<List<FavoritesResponseDto>> getUserFavorites(@PathVariable String userId, @RequestParam(defaultValue = "0",required = false) int offset, @RequestParam(defaultValue = "12",required = false) int limit) throws IOException{
		List<FavoritesResponseDto> response  = favoriteService.getUserFavorites(userId,offset,limit);
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
