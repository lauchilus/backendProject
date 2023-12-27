package com.gamelist.main.auth;

import java.security.Principal;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gamelist.main.models.user.User;
import com.gamelist.main.models.user.UserResponseDto;
import com.gamelist.main.models.user.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

@Controller
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;

	@GetMapping("/test")
	public String getPrincipalName(Principal principal) {
		
		
		return principal.getName();
	}
	
	
	@PostMapping("/register")
	public ResponseEntity<UserResponseDto> register(@RequestBody RegisterDto register){
		UserResponseDto user = userService.saveUser(register);
		return ResponseEntity.ok(user);
	}
	
}
