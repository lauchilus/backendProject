package com.gamelist.main.igbd;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/games")
public class IgdbController {

	@Autowired
	private IgdbService igdb;
	
	
	@GetMapping("/list")
	public ResponseEntity games(@RequestParam int offset) {
		String a = igdb.listGames(offset);
		return ResponseEntity.ok(a);
	}
	
	@GetMapping("/test")
	public String test() {
		return "funciona";
	}
	
	@GetMapping("/{id}")
	public ResponseEntity game(@PathVariable int id) throws IOException {
		String a = igdb.searchGameById(id);
		return ResponseEntity.ok(a);
	}
	
	@GetMapping("/a/{name}")
	public ResponseEntity game2(@PathVariable String name) throws IOException {
		String a = igdb.searchGame(name);
		return ResponseEntity.ok(a);
	}
	
}
