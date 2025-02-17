package com.example.demo;

import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BackgroundTask {

	private final UserRepository userRepository;

	@Scheduled(fixedRate = 5, timeUnit = TimeUnit.SECONDS)
	public void work() {
		userRepository.findById(1l);
	}

}