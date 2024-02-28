package com.gamelist.main.exceptions;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({JsonMappingException.class, JsonProcessingException.class})
    public ResponseEntity<ErrorDto> jsonMappingException(JsonMappingException exception){
        ErrorDto response = new ErrorDto(exception.getMessage(), "p-500");
        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorDto> nullPointerException(NullPointerException exception){
        ErrorDto response = new ErrorDto(exception.getMessage(), "Null pointer");
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationException(MethodArgumentNotValidException exception) {
        ErrorDto response = new ErrorDto(exception.getMessage(), "Invalid arguments");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PersonalizedExceptions.class)
    public ResponseEntity<ErrorDto> handleValidationException(PersonalizedExceptions exception) {
        ErrorDto response = new ErrorDto(exception.getMessage(), "Invalid arguments");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleValidationException(ConstraintViolationException exception) {
        ErrorDto response = new ErrorDto(exception.getMessage(), "Invalid arguments");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDto> handleValidationException(MethodArgumentTypeMismatchException exception) {
        ErrorDto response = new ErrorDto(exception.getMessage(), "Invalid arguments");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
