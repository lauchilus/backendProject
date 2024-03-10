package com.gamelist.main.models.played;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@Controller
@CrossOrigin("http://localhost:4200")
@RequestMapping("/api/v1/played")
@Tag(name="Played")
@RequiredArgsConstructor
public class PlayedController {
	

	private final PlayedService playedService;
	
	@Operation(summary = "add a played game", description = "blabla")
	@PostMapping("/{user}")
	public ResponseEntity<PlayedPostResponse> addPlayed(@PathVariable String user,@RequestParam long gameID) throws Exception{
		Played played = playedService.addPlayed(user,gameID);
		PlayedPostResponse response = new PlayedPostResponse(played.getId(), played.getGameId(), played.getFinish_date());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@Operation(summary = "get all played games from user", description = "blabla")
	@GetMapping("/{user}")
	public ResponseEntity<List<PlayedResponse>> getAllUserPlayed(@PathVariable String user,@RequestParam(defaultValue = "0",required = false) int offset, @RequestParam(defaultValue = "12",required = false) int limit) throws IOException{
		List<PlayedResponse> response = playedService.getAllUserPlayed(user,PageRequest.of(offset, limit));
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@DeleteMapping("/{played}")
	public ResponseEntity<Map<String, String>> deletePlayed(@PathVariable String played){
		playedService.deletePlayed(played);
		Map<String, String> responseBody = new HashMap<>();
		responseBody.put("msg", "Game deleted from played");
		return new ResponseEntity<Map<String, String>>(responseBody,HttpStatus.OK);
	}

	@GetMapping("/profile/total/{userId}")
	public ResponseEntity<Map<String, Integer>> getCountReviews(@PathVariable String userId){
		Integer total = playedService.countUserReviews(userId);
		Map<String, Integer> responseBody = new HashMap<>();
		responseBody.put("total", total);
		return new ResponseEntity<Map<String, Integer>>(responseBody,HttpStatus.OK);

	}
}
