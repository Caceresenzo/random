package com.example.demo;

import java.time.Duration;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.SneakyThrows;

@EnableScheduling
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	ApplicationRunner runner(
		UserRepository repository
	) {
		return (args) -> {
			new Thread(new Runnable() {

				@SneakyThrows
				public void run() {
					Thread.sleep(Duration.ofSeconds(2));

					repository.findById(1l);
				}

			}).start();
		};
	}

}