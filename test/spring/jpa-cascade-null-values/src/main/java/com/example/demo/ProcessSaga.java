package com.example.demo;

import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import com.example.demo.domain.GroupRepository;
import com.example.demo.domain.Pet;
import com.example.demo.domain.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessSaga {

	private final GroupRepository groupRepository;
	private final TransactionTemplate transactionTemplate;

	//	@Transactional
	public void work(long id) {
		log.info("initial save");
		final var group = groupRepository.findById(id).orElseThrow();
		group.clearUsers();

		final var group1 = transactionTemplate.execute((status) -> {
			return groupRepository.save(group);
		});

		final var user = new User("Hello #%s".formatted(id));
		group1.addUser(user);

		log.info("with user save");
		transactionTemplate.executeWithoutResult((status) -> {
			groupRepository.save(group1);
		});

		log.info("user.id={}", user.getId());
		log.info("getUsers(0).id={}", group.getUsers().get(0).getId());

		//		final var pet = new Pet("World #%s".formatted(id));
		//		user.addPet(pet);
		//
		//		log.info("with pet save");
		////		group.triggerModifications();
		////		System.out.println(group.getUsers().get(0));
		//		groupRepository.save(group);
		//
		//		log.info("user.id={}", user.getId());
	}

}