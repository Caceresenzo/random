package com.example.demo;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserInitialData implements ApplicationRunner {

	private final UserRepository repository;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (repository.existsById(1l)) {
			return;
		}

		repository.save(
			new User()
				.setId(1)
		);
	}

}