package repository;

import java.util.List;
import java.util.UUID;

import models.User;

public interface UserRepository {

	User findById(UUID id);

	List<User> findAll();
}
