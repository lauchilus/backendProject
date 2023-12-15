package com.gamelist.main.models.played;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/played")
public class PlayedController {
	
	@Autowired
	private PlayedService playedService;
	
	@PostMapping
	public ResponseEntity<PlayedPostResponse> addPlayed(@RequestParam long user,@RequestParam long gameID){
		Played played = playedService.addPlayed(user,gameID);
		PlayedPostResponse response = new PlayedPostResponse(played.getId(), played.getGame_id(), played.getFinish_date());
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{user}")
	public ResponseEntity<List<PlayedPostResponse>> getAllUserPlayed(@PathVariable long user){
		List<Played> played = playedService.getAllUserPlayed(user);
		List<PlayedPostResponse> response = played.stream()
		        .map((Played played2) -> new PlayedPostResponse(played2.getId(), played2.getGame_id(), played2.getFinish_date()))
		        .collect(Collectors.toList());
		return ResponseEntity.ok(response);
	}
}
