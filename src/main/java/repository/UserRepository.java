package repository;

import java.util.List;
import java.util.UUID;

import models.User;

public interface UserRepository {
	
	UUID save(User user);

	User findById(UUID id);

	List<User> findAll();
}