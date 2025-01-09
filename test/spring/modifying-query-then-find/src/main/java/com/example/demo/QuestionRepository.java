package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Long> {

	@Modifying(
		clearAutomatically = true
	// clearAutomatically = false
	)
	@Query("""
			UPDATE
			Question q
		SET
			q.value = q.value + 1
		WHERE
			q.id = :id
		""")
	void update(long id);

}