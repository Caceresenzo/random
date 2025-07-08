package com.example.enumtest;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

	@EntityGraph(attributePaths = { "organization" })
	@Query("SELECT u FROM User u WHERE u.role = :role")
	List<User> findAllByRole(Role role);

	@EntityGraph(attributePaths = { "organization" })
	@Query("SELECT u FROM User u WHERE u.role = com.example.enumtest.Role.ADMIN")
	List<User> findAllAdmin();

	@EntityGraph(attributePaths = { "organization" })
	@Query("SELECT u FROM User u LEFT JOIN u.organization o WHERE o.stage = com.example.enumtest.Stage.COMPANY")
	List<User> findAllFromCompany();

	@EntityGraph(attributePaths = { "organization" })
	@Query("SELECT u FROM User u LEFT JOIN u.organization o WHERE o.stage = :stage")
	List<User> findAllFrom(@Param("stage") Stage stage);

}