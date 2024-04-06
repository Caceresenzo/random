package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserRestController {

	private final UserService userService;
	
	@GetMapping("users/{id}")
	public User increment(
		@PathVariable long id
	) {
		return userService.increment(id);
	}

}