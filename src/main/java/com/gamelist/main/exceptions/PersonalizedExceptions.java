package com.gamelist.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;


public class PersonalizedExceptions extends RuntimeException {

	public  PersonalizedExceptions(String message){
		super(message);
	}




	
	
}
