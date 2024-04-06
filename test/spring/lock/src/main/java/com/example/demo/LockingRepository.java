package com.example.demo;

import java.util.Optional;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import jakarta.persistence.LockModeType;

@NoRepositoryBean
public interface LockingRepository<T, ID> extends Repository<T, ID> {

	Optional<T> findById(ID id, LockModeType lockMode);

}