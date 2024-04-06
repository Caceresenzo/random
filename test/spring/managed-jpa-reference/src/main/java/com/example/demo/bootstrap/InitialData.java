package com.example.demo.bootstrap;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.demo.domain.article.Article;
import com.example.demo.domain.article.ArticleRepository;
import com.example.demo.domain.comment.Comment;
import com.example.demo.domain.comment.CommentRepository;

import lombok.RequiredArgsConstructor;

@Order(10)
@Component
@RequiredArgsConstructor
public class InitialData implements ApplicationRunner {

	private final ArticleRepository articleRepository;
	private final CommentRepository commentRepository;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (articleRepository.count() != 0) {
			return;
		}

		final var article = articleRepository.save(
			new Article()
				.setTitle("Hello")
		);

		commentRepository.save(
			new Comment()
				.setArticle(article)
				.setContent("World")
		);
	}

}