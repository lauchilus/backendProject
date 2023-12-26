package com.gamelist.main.models.favorites;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@CrossOrigin
@RequestMapping("/favorite")
public class FavoriteController {

	@Autowired
	private FavoriteService favoriteService;

	@GetMapping
	public ResponseEntity<List<FavoritesResponseDto>> getUserFavorites(@RequestParam long userId) throws IOException{
		List<FavoritesResponseDto> response  = favoriteService.getUserFavorites(userId);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping
	public ResponseEntity<FavoritesCreateDto> addFavoriteToUser(@RequestParam long userId,@RequestParam long gameId) throws Exception{
		FavoritesCreateDto response = favoriteService.addFavoriteToUser(userId,gameId);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("profile")
	public ResponseEntity<List<FavoritesResponseDto>> getTop5Favorites(@RequestParam long userId) throws IOException{
		List<FavoritesResponseDto> response  = favoriteService.getUserTopFavorites(userId);
		return ResponseEntity.ok(response);
	}
	
	
}