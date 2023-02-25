package repository.impl;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import models.BaseEntity;
import repository.EntityRepository;

public abstract class AbstractEntityRepository<T extends BaseEntity> implements EntityRepository<T> {
	
	private final EntityManagerFactory entityManagerFactory;
	
	protected AbstractEntityRepository(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	@Override
	public UUID save(T entity) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			entityManager.persist(entity);
			transaction.commit();
		} catch (Exception e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			throw e;
		} finally {
			entityManager.close();
		}
		return entity.getId();
	}
	
	protected T findById(UUID id, Class<T> entityType) {
	    EntityManager entityManager = entityManagerFactory.createEntityManager();
	    T entity = entityManager.find(entityType, id);
	    entityManager.close();
	    return entity;
	}
	
	protected List<T> findAll(Class<T> entityType) {
	    EntityManager entityManager = entityManagerFactory.createEntityManager();
	    String hql = "SELECT e FROM " + entityType.getSimpleName() + " e";
	    TypedQuery<T> query = entityManager.createQuery(hql, entityType);
	    List<T> entities = query.getResultList();
	    entityManager.close();
	    return entities;
	}

}
