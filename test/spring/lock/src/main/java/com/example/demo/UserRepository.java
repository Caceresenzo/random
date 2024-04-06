package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import jakarta.persistence.LockModeType;

public interface UserRepository extends JpaRepository<User, Long>, LockingRepository<User, Long> {

	@Override
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<User> findById(Long id);

	@Override
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	List<User> findAll();

}