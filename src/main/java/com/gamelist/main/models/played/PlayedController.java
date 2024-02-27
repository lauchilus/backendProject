package com.gamelist.main.models.played;

import java.io.IOException;
import java.util.List;

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
@CrossOrigin("*")
@RequestMapping("/played")
@Tag(name="Played")
@RequiredArgsConstructor
public class PlayedController {
	

	private final PlayedService playedService;
	
	@Operation(summary = "add a played game", description = "blabla")
	@PostMapping
	public ResponseEntity<PlayedPostResponse> addPlayed(@RequestParam String user,@RequestParam long gameID) throws Exception{
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
	
	//TODO deletes

	@DeleteMapping("/{played}")
	public ResponseEntity<String> deletePlayed(@PathVariable String played){
		playedService.deletePlayed(played);
		return new ResponseEntity<String>("Game deleted from played",HttpStatus.OK);
	}
}
