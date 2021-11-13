package uservice.forum.users;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

import uservice.forum.users.entity.User;
import uservice.forum.users.repository.UserRepository;

@SpringBootApplication
public class UsersServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(UsersServiceApplication.class, args);
	}
	
	@Bean
	CommandLineRunner lineRunner(UserRepository userRepository, RepositoryRestConfiguration restConfiguration) {
		return (args) -> {
			restConfiguration.exposeIdsFor(User.class);
			
			Arrays.asList(
					new User().setName("Enzo").setAge(20),
					new User().setName("Dorian").setAge(20),
					new User().setName("Quentin").setAge(8) //
			).forEach(userRepository::save);
		};
	}
	
}