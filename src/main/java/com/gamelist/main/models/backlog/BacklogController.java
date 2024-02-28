package com.gamelist.main.models.backlog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@CrossOrigin("http://localhost:4200")
@Tag(name = "Backlog")
@RequestMapping(path = "/api/v1/backlogs")
public class BacklogController {

	@Autowired 
	private BacklogService backlogService;
	
	@Operation(summary = "create a backlog", description = "blabla")
	@PostMapping("/{user}")
	public ResponseEntity<BacklogUserResponseDto> addGameToBacklog(@PathVariable String user, @RequestParam long gameId) throws JsonMappingException, JsonProcessingException {
		BacklogUserResponseDto blg = backlogService.addGameToBacklog(user,gameId);
		return ResponseEntity.status(HttpStatus.CREATED).body(blg);
	}
	
	@Operation(summary = "get all backlogs games from user", description = "blabla")
	@GetMapping("/{userId}")
	public ResponseEntity<List<BacklogUserResponseDto>> getAllBacklogFromUser(@PathVariable String userId,@RequestParam(defaultValue = "0",required = false) int offset, @RequestParam(defaultValue = "12",required = false) int limit) throws JsonMappingException, JsonProcessingException{
		List<BacklogUserResponseDto> response = backlogService.getAllBacklogsFromUser(userId, PageRequest.of(offset,limit));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/{backlogId}")
	public ResponseEntity<String> deleteBacklog(@PathVariable String backlogId){
		backlogService.delete(backlogId);
		return new ResponseEntity<String>("Game deleted from backlog",HttpStatus.OK);
	}
}
