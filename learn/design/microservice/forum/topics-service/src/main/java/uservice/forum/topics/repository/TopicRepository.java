package uservice.forum.topics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import uservice.forum.topics.entity.Topic;

@RepositoryRestResource
public interface TopicRepository extends JpaRepository<Topic, Long> {
	
	List<Topic> findAllByOwnerId(@Param("owner") long ownerId);
	
}