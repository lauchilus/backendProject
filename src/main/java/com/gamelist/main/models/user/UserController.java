package com.gamelist.main.models.user;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.gamelist.main.auth.RegisterDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@CrossOrigin("https://gamelist-app.netlify.app/")
@RequestMapping("/api/v1/users")
@Tag(name = "User")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	
	@Operation(summary = "create user", description = "blabla")
	@PostMapping
	public ResponseEntity<String> createUser(@RequestBody RegisterDto userDto ) throws IOException{
		String user = userService.saveUser(userDto);
		System.out.println(user+"*******************");
		return ResponseEntity.status(HttpStatus.CREATED).body("created!");
	}
	
	@PutMapping("/{userId}")
	@Operation(summary = "update profile ", description = "blabla")
	public ResponseEntity<User> updateProfile(@PathVariable String userId,@ModelAttribute UpdateUser updateUser) throws IOException{
		User user = userService.updateProfile(userId, updateUser);
		return ResponseEntity.ok(user);
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "get user info by id", description = "blabla")
	public ResponseEntity<UserResponseDto> getUser(@PathVariable String id){
		UserResponseDto user = userService.getUserReference(id);
		return ResponseEntity.ok(user);
	}
	
	@GetMapping("/test")
	public ResponseEntity test(@RequestParam String id) {
		User user  = userService.getUser(id);
		return ResponseEntity.ok(user);
	}
	
	@Operation(summary = "verify if a user exists by username", description = "blabla")
	@GetMapping("/verify")
	public ResponseEntity<?> verifyUsername(@RequestParam String username){
		boolean exist = userService.existUsername(username);
		System.out.println(username);
		return ResponseEntity.ok(exist);
	}
}
