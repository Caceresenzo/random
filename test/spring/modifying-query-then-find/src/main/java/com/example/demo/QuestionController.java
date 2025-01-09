package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class QuestionController {

	private final QuestionRepository questionRepository;
	private final QuestionService questionService;

	@GetMapping("create")
	public long create() {
		final var question = questionRepository.save(new Question());

		return question.getId();
	}

	@GetMapping("update")
	public void update() {
		for (var question : questionRepository.findAll()) {
			log.info("before - id={} value={}", question.getId(), question.getValue());

			question = questionService.increment(question.getId());

			log.info("after - id={} value={}", question.getId(), question.getValue());
		}
	}

}