package repository;

import java.util.List;
import java.util.UUID;

import models.Event;

public interface EventRepository {
	
	UUID save(Event event);

	Event findById(UUID id);
	
	List<Event> findAll();
}
