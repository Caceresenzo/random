package com.example.demo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {

	private final QuestionRepository repository;

	@Transactional(propagation = Propagation.REQUIRED)
	public Question increment(long id) {
		repository.update(id);

		final var question = repository.findById(id).get();
		question.setName("hello" + System.currentTimeMillis());

		return repository.save(question);
	}

}