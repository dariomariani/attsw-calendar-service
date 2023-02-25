package repository.impl;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.EntityManagerFactory;
import models.User;

public class UserRepositoryImpl extends AbstractEntityRepository<User> {
	
	public UserRepositoryImpl(EntityManagerFactory entityManagerFactory) {
		super(entityManagerFactory);
	}

	@Override
	public User findById(UUID id) {
		return this.findById(id, User.class);
	}

	@Override
	public List<User> findAll() {
		return this.findAll(User.class);
	}

}
