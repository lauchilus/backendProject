package com.gamelist.main.models.user;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	
	@PostMapping
	public ResponseEntity<User> createUser(@RequestParam String username, MultipartFile avatar ) throws IOException{
		User user = userService.createUser(username,avatar);
		
		return ResponseEntity.ok(user); 
	}
	
	@PutMapping
	public ResponseEntity<User> updateProfile(@RequestParam String userId,@RequestBody MultipartFile avatar,@RequestParam String username, @RequestParam String bio) throws IOException{
		User user = userService.updateProfile(userId, avatar,username,bio);
		System.out.println(avatar);
		return ResponseEntity.ok(user);
	}
	
	@GetMapping
	public ResponseEntity<UserResponseDto> getUser(@RequestParam String id){
		UserResponseDto user = userService.getUserReference(id);
		return ResponseEntity.ok(user);
	}
	
	@GetMapping("/test")
	public ResponseEntity test(@RequestParam String id) {
		User user  = userService.getUser(id);
		return ResponseEntity.ok(user);
	}
}
