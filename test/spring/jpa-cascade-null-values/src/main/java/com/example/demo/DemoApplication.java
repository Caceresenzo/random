package com.example.demo;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.support.TransactionTemplate;

import com.example.demo.domain.Group;
import com.example.demo.domain.GroupRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	@Order(1)
	ApplicationRunner cleanup(
		//		JpaRepository<Pet, Long> petRepository,
		//		JpaRepository<Pet, Long> userRepository,
		GroupRepository groupRepository,
		TransactionTemplate transactionTemplate
	) {
		return (args) -> {
			log.info("cleanup");

			transactionTemplate.executeWithoutResult((status) -> {
				//				petRepository.deleteAll();
				//				userRepository.deleteAll();
				groupRepository.deleteAll();
			});
		};
	}

	@Bean
	@Order(2)
	ApplicationRunner prepare(
		GroupRepository groupRepository
	) {
		return (args) -> {
			log.info("prepare");

			groupRepository.saveAndFlush(
				new Group("Root")
			);
		};
	}

	@Bean
	@Order(3)
	ApplicationRunner run(
		GroupRepository groupRepository,
		ProcessSaga processSaga
	) {
		return (args) -> {
			log.info("run");

			final var id = groupRepository.findAll(PageRequest.of(0, 1))
				.stream()
				.findFirst()
				.orElseThrow()
				.getId();

			try {
				processSaga.work(id);
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		};
	}

}