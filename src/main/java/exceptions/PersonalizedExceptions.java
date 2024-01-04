package exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PersonalizedExceptions extends RuntimeException {

	public  PersonalizedExceptions(String message){
		super(message);
	}
	
	
}
