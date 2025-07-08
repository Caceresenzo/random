package com.example.enumtest;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EnumTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnumTestApplication.class, args);
	}

	@Bean
	ApplicationRunner applicationRunner(
		UserRepository userRepository,
		OrganizationRepository organizationRepository
	) {
		return (args) -> {
			userRepository.deleteAll();
			userRepository.flush();
			organizationRepository.deleteAll();
			organizationRepository.flush();

			final var github = organizationRepository.save(new Organization("GitHub", Stage.COMPANY));

			userRepository.save(new User("GitHub User A", Role.USER, github));
			userRepository.save(new User("GitHub User B", Role.USER, github));
			userRepository.save(new User("GitHub Admin", Role.ADMIN, github));
			organizationRepository.flush();

			final var google = organizationRepository.save(new Organization("Google", Stage.MONOPOLY));

			userRepository.save(new User("Google A", Role.USER, google));
			userRepository.save(new User("Google B", Role.USER, google));
			userRepository.save(new User("Google Admin", Role.ADMIN, google));

			System.out.println("Users ---");
			var users = userRepository.findAllByRole(Role.USER);
			users.forEach((user) -> System.out.println(">> " + user.getName()));

			System.out.println("Admins ---");
			var admins = userRepository.findAllAdmin();
			admins.forEach((user) -> System.out.println(">> " + user.getName()));

			System.out.println("From Companies ---");
			var companyUsers = userRepository.findAllFromCompany();
			companyUsers.forEach((user) -> System.out.println(">> " + user.getName()));

			System.out.println("From Monopolies ---");
			var monopolyUsers = userRepository.findAllFrom(Stage.MONOPOLY);
			monopolyUsers.forEach((user) -> System.out.println(">> " + user.getName()));
		};
	}

}