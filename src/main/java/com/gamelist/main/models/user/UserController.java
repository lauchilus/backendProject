package com.gamelist.main.models.user;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	
	@PostMapping
	public ResponseEntity<UserResponseDto> createUser(@RequestParam String username, MultipartFile avatar ) throws IOException{
		User user = userService.createUser(username,avatar);
		UserResponseDto dto = new UserResponseDto(user.getUsername(),user.getBio(),user.getImage().getImageUrl());
		return ResponseEntity.ok(dto); 
	}
	
	@PutMapping
	public ResponseEntity<User> updateProfile(@RequestParam long userId,@RequestBody UpdateUser update) throws IOException{
		User user = userService.updateProfile(userId, update);
		return ResponseEntity.ok(user);
	}
	
	@GetMapping
	public ResponseEntity<UserResponseDto> getUser(@RequestParam long id){
		User user = userService.getUserReference(id);
		UserResponseDto dto = new UserResponseDto(user.getUsername(),user.getBio(),user.getImage().getImageUrl());
		return ResponseEntity.ok(dto);
	}
	
	@GetMapping("/test")
	public ResponseEntity test() {
		return ResponseEntity.ok("AAAAAAAA");
	}
}
