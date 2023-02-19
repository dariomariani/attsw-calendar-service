package repository.impl;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import models.User;
import repository.UserRepository;

public class UserRepositoryImpl implements UserRepository {
	
	private final EntityManagerFactory entityManagerFactory;
	
	public UserRepositoryImpl(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	@Override
	public UUID save(User user) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			entityManager.persist(user);
			transaction.commit();
		} catch (Exception e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			throw e;
		} finally {
			entityManager.close();
		}
		return user.getId();
	}

	@Override
	public User findById(UUID id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		String hql = "SELECT u FROM User u WHERE u.id = :id";
		TypedQuery<User> query = entityManager.createQuery(hql, User.class);
		query.setParameter("id", id);
		var user = query.getSingleResult();
		entityManager.close();
		return user;
	}

	@Override
	public List<User> findAll() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		String hql = "SELECT u FROM User u";
		TypedQuery<User> query = entityManager.createQuery(hql, User.class);
		List<User> users = query.getResultList();
		entityManager.close();
		return users;
	}

}
