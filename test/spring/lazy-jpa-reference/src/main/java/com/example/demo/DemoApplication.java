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
	private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Override
	public void run(String... args) throws Exception {
		log.info("----------------");
		log.info("Saving");
		
		{
			postRepository.deleteAll();
			userRepository.deleteAll();

			User user = userRepository.save(new User().setName("Hello"));
			
			postRepository.save(new Post().setUser(user).setContent("A"));
			
			userRepository.flush();
			postRepository.flush();
		}

		log.error("------------------------");
		
		{
			for (Post post : postRepository.findAll()) {
				log.info("Before print");
				log.error("{}", post.getUser().getIdx());
				log.error("{}", post.getUser().getId());
				log.error("{}", post.getUser().getIdAsString());
				log.info("After print");
				final var user = post.getUser();
				System.out.println();
			}
		}
	}
	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
}