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
			
			postRepository.save(new Post().setUser(user).setContent("Lorem"));
			postRepository.save(new Post().setUser(user).setContent("Ipsum"));

			user = userRepository.save(new User().setName("World"));
			
			postRepository.save(new Post().setUser(user).setContent("meroL"));
			postRepository.save(new Post().setUser(user).setContent("muspI"));
			
			userRepository.flush();
			postRepository.flush();
		}

		log.info("----------------");
		log.info("Only ID");
		
		{
			for (Post post : postRepository.findAll()) {
				System.out.println(post.getUser().getId());
			}
		}

		log.info("----------------");
		log.info("Name");
		
		{
			for (Post post : postRepository.findAll()) {
				System.out.println(post.getUser().getName());
			}
		}
	}
	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
}