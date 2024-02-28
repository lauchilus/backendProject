package com.gamelist.main.igbd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@CrossOrigin("http://localhost:4200/")
@RequestMapping("/api/v1/games")
@Tag(name="Games")
@RequiredArgsConstructor
public class IgdbController {

	private final IgdbService igdb;
	
	@Operation(summary = "get games ", description = "blabla")
	@GetMapping()
	public ResponseEntity<List<SearchGameListDto>> games(@RequestParam int offset) throws JsonMappingException, JsonProcessingException {
		String list = igdb.listGames(offset);
		List<SearchGameListDto> response = igdb.processGameToListDto(list);
		return ResponseEntity.ok(response);
	}
	
	@Operation(summary = "get game details", description = "blabla")
	@GetMapping("/details/{id}")
	public ResponseEntity<SearchGameDetailsDto> game(@PathVariable long id) throws IOException {
		SearchGameDetailsDto a = igdb.searchGameDetails(id);
		return ResponseEntity.ok(a);
	}
	
	@Operation(summary = "get games that match the name", description = "blabla")
	@GetMapping("/a/{name}")
	public ResponseEntity<List<SearchGameListDto>> gameDetails(@PathVariable String name,@RequestParam(defaultValue = "0",required = false) Integer offset) throws IOException {
		List<SearchGameListDto> a = igdb.searchGameListByName(name,offset);
		return ResponseEntity.ok(a);
	}
	
}
