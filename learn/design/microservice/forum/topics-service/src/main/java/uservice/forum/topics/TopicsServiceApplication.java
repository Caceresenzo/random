package uservice.forum.topics;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

import uservice.forum.topics.entity.Topic;
import uservice.forum.topics.remote.UsersService;
import uservice.forum.topics.repository.TopicRepository;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class TopicsServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(TopicsServiceApplication.class, args);
	}
	
	@Bean
	CommandLineRunner lineRunner(TopicRepository topicRepository, UsersService usersService, RepositoryRestConfiguration restConfiguration) {
		return (args) -> {
			restConfiguration.exposeIdsFor(Topic.class);
			
			usersService.findAll()
					.getContent()
					.stream()
					.map((user) -> new Topic()
							.setTitle("Lemme 1: Flo a toujours raison")
							.setOwnerId(user.getId()))
					.forEach(topicRepository::save);
		};
	}
	
}