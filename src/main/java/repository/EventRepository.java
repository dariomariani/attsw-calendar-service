package repository;

import java.util.List;
import java.util.UUID;

import models.Event;

public interface EventRepository {

	Event findById(UUID id);
	
	List<Event> findAll();
}
