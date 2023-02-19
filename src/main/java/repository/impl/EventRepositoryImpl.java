package repository.impl;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import models.Event;
import repository.EventRepository;

public class EventRepositoryImpl implements EventRepository {

	private final EntityManagerFactory entityManagerFactory;

	public EventRepositoryImpl(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	@Override
	public UUID save(Event event) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			entityManager.persist(event);
			transaction.commit();
		} catch (Exception e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			throw e;
		}
		return event.getId();
	}

	@Override
	public Event findById(UUID id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		String hql = "SELECT e FROM Event e WHERE e.id = :id";
		TypedQuery<Event> query = entityManager.createQuery(hql, Event.class);
		query.setParameter("id", id);
		var event = query.getSingleResult();
		entityManager.close();
		return event;
	}

	@Override
	public List<Event> findAll() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		String hql = "SELECT e FROM Event e";
		TypedQuery<Event> query = entityManager.createQuery(hql, Event.class);
		List<Event> events = query.getResultList();
		entityManager.close();
		return events;
	}

}
