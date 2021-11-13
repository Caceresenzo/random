package uservice.forum.topics.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;

import uservice.forum.topics.model.User;

@FeignClient("USERS-SERVICE")
public interface UsersService {
	
	@GetMapping("/users")
	PagedModel<User> findAll();
	
}