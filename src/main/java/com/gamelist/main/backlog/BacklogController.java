package com.gamelist.main.backlog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Controller
@RequestMapping("/backlog")
public class BacklogController {

	@Autowired
	private BacklogService backlogService;
	
	
	@PostMapping
	public ResponseEntity<Backlog> addGameToBacklog(@RequestParam long userId, @RequestParam long gameId) {
		Backlog blg = backlogService.addGameToBacklog(userId,gameId);
		return ResponseEntity.ok(blg);
	}
	
	@GetMapping
	public ResponseEntity<BacklogUserResponseDto> getAllBacklogFromUser(@RequestParam long userId) throws JsonMappingException, JsonProcessingException{
		BacklogUserResponseDto response = backlogService.getAllBacklogsFromUser(userId);
		return ResponseEntity.ok(response);
	}
}
