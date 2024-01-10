package com.gamelist.main.auth;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gamelist.main.models.user.UserService;

@Controller
@CrossOrigin("*")
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;

	@GetMapping("/test")
	public String getPrincipalName(Principal principal) {
		
		
		return principal.getName();
	}
	
	
	@PostMapping("register")
	public ResponseEntity<String> register(@RequestBody RegisterDto register){
		String user = userService.saveUser(register);
		return ResponseEntity.ok(user);
	}
	
	@CrossOrigin
	@GetMapping("/verifyuser")
	public ResponseEntity<Boolean> verifyUser(@RequestParam String username,@RequestParam String email){
		if(userService.verifyEmail(email) || userService.existUsername(username)) {
			return ResponseEntity.ok(true);
		}
		
		return ResponseEntity.ok(false);
	}
	
}
