package com.example.demo.playground;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.comment.CommentCreatedEvent;
import com.example.demo.domain.comment.CommentRepository;

import lombok.RequiredArgsConstructor;

@Order(20)
@Component
@RequiredArgsConstructor
public class Entrypoint implements ApplicationRunner {

	private final CommentRepository commentRepository;
	private final ApplicationEventPublisher eventPublisher;

	@Override
	@Transactional
	public void run(ApplicationArguments args) throws Exception {
		final var comment = commentRepository.findAll(PageRequest.of(0, 1)).stream().findFirst().get();

		eventPublisher.publishEvent(new CommentCreatedEvent(comment, this));
	}

}