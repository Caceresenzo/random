package com.example.demo.playground;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.article.Article;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Receiver {

	private final EntityManager entityManager;

	@Transactional(propagation = Propagation.REQUIRED)
	public void accept(Article article) {
		entityManager.refresh(article, LockModeType.PESSIMISTIC_WRITE);

		System.out.println(article);
	}

}