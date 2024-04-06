package com.example.demo.playground;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.demo.domain.comment.CommentCreatedEvent;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OnEvent {

	private final EntityManager entityManager;

	private final Receiver receiver;

	@TransactionalEventListener(fallbackExecution = true)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void onEvent(CommentCreatedEvent event) {
		final var comment = event.getComment();
		var article = comment.getArticle();

		if (!entityManager.contains(article)) {
			article = entityManager.find(article.getClass(), article.getId());
			Objects.requireNonNull(article, "article not found after find");
		}

		receiver.accept(article);
	}

}