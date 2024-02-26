package com.gamelist.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(name = "/JWT")
@Tag(name = "JWT")
public class TokenController {

	@Operation(summary = "create a jwt", description = "blabla")
	@GetMapping
	public String getTestJWT(@RequestParam String id) throws FirebaseAuthException {
		FirebaseApp.initializeApp();

		return FirebaseAuth.getInstance().createCustomToken(id);
		// Send token back to client

	}
}
