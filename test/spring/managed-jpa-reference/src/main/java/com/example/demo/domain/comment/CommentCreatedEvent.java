package com.example.demo.domain.comment;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@SuppressWarnings("serial")
public class CommentCreatedEvent extends ApplicationEvent {
	
	@Getter
	private final Comment comment;

	public CommentCreatedEvent(Comment comment, Object source) {
		super(source);
		
		this.comment = comment;
	}

}