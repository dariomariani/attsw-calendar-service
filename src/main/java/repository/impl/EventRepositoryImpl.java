package repository.impl;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.EntityManagerFactory;
import models.Event;

public class EventRepositoryImpl extends AbstractEntityRepository<Event> {

    public EventRepositoryImpl(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    @Override
    public Event findById(UUID id) {
        return this.findById(id, Event.class);
    }

    @Override
    public List<Event> findAll() {
        return this.findAll(Event.class);
    }

}
