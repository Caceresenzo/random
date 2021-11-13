package uservice.forum.users.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import uservice.forum.users.entity.User;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long> {
	
	List<User> findAllByAgeLessThan(@Param("age") int age);
	
}