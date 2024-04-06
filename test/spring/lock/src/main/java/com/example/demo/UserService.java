package com.example.demo;

import java.time.Duration;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository repository;

	//	@Transactional(isolation = Isolation.REPEATABLE_READ)
	@Transactional
	public User increment(long id) {
		final var user = repository.findById(id).get();

		doIncrement("function", user);

		return user;
	}

	@Scheduled(fixedDelay = 1000)
	//	@Transactional(isolation = Isolation.REPEATABLE_READ)
	@Transactional
	public void watch() {
		for (final var user : repository.findAll()) {
			doIncrement("scheduled", user);
		}
	}

	@SneakyThrows
	public void doIncrement(String where, User user) {
		final var oldValue = user.getValue();
		final var newValue = oldValue + 1;

		log.info("increment - where={} userId={} oldValue={} newValue={}", where, user.getId(), oldValue, newValue);

		user.setValue(newValue);

		Thread.sleep(Duration.ofSeconds(3));
		repository.save(user);
	}

}