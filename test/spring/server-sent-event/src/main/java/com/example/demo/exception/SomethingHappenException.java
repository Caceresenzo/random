package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty;
import lombok.experimental.StandardException;

@SuppressWarnings("serial")
@StandardException
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SomethingHappenException extends RuntimeException {
	
	@ResponseErrorProperty
	public String getWhy() {
		return "bad xyz on abc";
	}

}