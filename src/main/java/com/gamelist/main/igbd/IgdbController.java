package com.gamelist.main.igbd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

@Controller
@CrossOrigin("*")
@RequestMapping("/games")
public class IgdbController {

	@Autowired
	private IgdbService igdb;
	
	
	@GetMapping()
	public ResponseEntity<List<SearchGameListDto>> games(@RequestParam int offset) throws JsonMappingException, JsonProcessingException {
		String list = igdb.listGames(offset);
		List<SearchGameListDto> response = igdb.processGameToListDto(list);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/details")
	public ResponseEntity<SearchGameDetailsDto> game(@RequestParam long id) throws IOException {
		SearchGameDetailsDto a = igdb.searchGameDetails(id);
		return ResponseEntity.ok(a);
	}
	
	@GetMapping("/a/{name}")
	public ResponseEntity<List<SearchGameListDto>> gameDetails(@PathVariable String name,@RequestParam int offset) throws IOException {
		List<SearchGameListDto> a = igdb.searchGameListByName(name,offset);
		return ResponseEntity.ok(a);
	}
	
}
