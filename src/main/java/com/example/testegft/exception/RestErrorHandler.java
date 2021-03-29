package com.example.testegft.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javassist.NotFoundException;

@ControllerAdvice
public class RestErrorHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Erro> processIllegalArgument(IllegalArgumentException e) {
		Erro exception = new Erro();
		exception.setMessage(e.getMessage());
		return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Erro> processNotFound(NotFoundException e) {
		Erro exception = new Erro();
		exception.setMessage(e.getMessage());
		return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
	}
}
