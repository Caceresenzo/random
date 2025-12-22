package com.example.demo;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EnvParsingTestApplication {

	public static void main(String[] args) {
		//		Dotenv.configure()
		//			.ignoreIfMissing()
		//			.systemProperties()
		//			.load();

		SpringApplication.run(EnvParsingTestApplication.class, args);
	}

	@Bean
	ApplicationRunner applicationRunner(SomeConfiguration someConfiguration) {
		return (args) -> {
			System.out.println("aKey: " + someConfiguration.getAKey());
			System.out.println("deepKey: " + someConfiguration.getNestedGroup().getDeepKey());
		};
	}

}