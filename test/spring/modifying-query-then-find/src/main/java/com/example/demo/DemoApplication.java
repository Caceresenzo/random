package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	@Autowired
	private QuestionRepository questionRepository;

	@Override
	public void run(String... args) throws Exception {
		questionRepository.deleteAll();
		questionRepository.flush();
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}