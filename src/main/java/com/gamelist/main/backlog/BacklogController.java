package com.gamelist.main.backlog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@CrossOrigin("*")
@Tag(name = "Backlog")
@RequestMapping(path = "/backlog")
public class BacklogController {

	@Autowired 
	private BacklogService backlogService;
	
	@Operation(summary = "create a backlog", description = "blabla")
	@PostMapping
	public ResponseEntity<BacklogUserResponseDto> addGameToBacklog(@RequestParam String user, @RequestParam long gameId) throws JsonMappingException, JsonProcessingException {
		BacklogUserResponseDto blg = backlogService.addGameToBacklog(user,gameId);
		return ResponseEntity.status(HttpStatus.CREATED).body(blg);
	}
	
	@Operation(summary = "get all backlogs games from user", description = "blabla")
	@GetMapping
	public ResponseEntity<List<BacklogUserResponseDto>> getAllBacklogFromUser(@RequestParam String userId) throws JsonMappingException, JsonProcessingException{
		List<BacklogUserResponseDto> response = backlogService.getAllBacklogsFromUser(userId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	//TODO DELETE ENDPOINT 
}
