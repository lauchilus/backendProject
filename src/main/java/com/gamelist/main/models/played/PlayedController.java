package com.gamelist.main.models.played;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@CrossOrigin
@RequestMapping("/played")
public class PlayedController {
	
	@Autowired
	private PlayedService playedService;
	
	@PostMapping
	public ResponseEntity<PlayedPostResponse> addPlayed(@RequestParam String user,@RequestParam long gameID) throws Exception{
		Played played = playedService.addPlayed(user,gameID);
		PlayedPostResponse response = new PlayedPostResponse(played.getId(), played.getGameId(), played.getFinish_date());
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{user}")
	public ResponseEntity<List<PlayedResponse>> getAllUserPlayed(@PathVariable String user,@RequestParam int page) throws IOException{
		List<PlayedResponse> response = playedService.getAllUserPlayed(user,PageRequest.of(page, 9));
		
		return ResponseEntity.ok(response);
	}
}
