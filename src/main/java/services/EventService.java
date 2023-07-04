package services;

import models.Event;
import models.User;
import repository.impl.EventRepositoryImpl;
import utils.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class EventService {

    private final EventRepositoryImpl eventRepository;

    public EventService(EventRepositoryImpl eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> findAll() {
        return this.eventRepository.findAll();
    }

    public List<Event> findEventByUserAndPeriod(User owner, LocalDateTime startPeriod, LocalDateTime endPeriod) {
        return this.eventRepository.findAll().stream()
                .filter(event -> event.getOwner().getId().equals(owner.getId()) && hasOverlappingPeriod(event, startPeriod, endPeriod))
                .collect(Collectors.toList());
    }

    public Event findEventById(UUID id) {
        return this.eventRepository.findById(id);
    }

    public void createEvent(Event newEvent) {
        validate(newEvent);
        eventRepository.save(newEvent);
    }

    public void updateEvent(Event updatedEvent) {
        var dbEvent = findEventById(updatedEvent.getId());
        if (dbEvent == null) {
            throw new IllegalArgumentException("Event not found");
        }
        dbEvent.setName(updatedEvent.getName());
        dbEvent.setStartsAt(updatedEvent.getStartsAt());
        dbEvent.setEndsAt(updatedEvent.getEndsAt());
        validate(dbEvent);
        eventRepository.save(dbEvent);
    }

    private void validate(Event event) {
        validateFields(event);
        checkOverlappingEvent(event);
    }

    private void validateName(Event event) {
        if (event.getName() == null || event.getName().isBlank())
            throw new IllegalArgumentException("EventName cannot be null nor blank.");
    }

    private void validateFields(Event event) {
        validateName(event);
        if (event.getStartsAt() == null) throw new IllegalArgumentException("StartsAt cannot be null.");
        if (event.getEndsAt() == null) throw new IllegalArgumentException("EndsAt cannot be null.");
        if (event.getOwner() == null) throw new IllegalArgumentException("Owner cannot be null.");
        if (event.getStartsAt().isAfter(event.getEndsAt()))
            throw new IllegalArgumentException("EndsAt must be after StartsAt.");
    }

    private boolean hasOverlappingPeriod(Event event, LocalDateTime startPeriod, LocalDateTime endPeriod) {
        return DateTimeUtil.isBetween(event.getStartsAt(), event.getEndsAt(), startPeriod)
                || DateTimeUtil.isBetween(event.getStartsAt(), event.getEndsAt(), endPeriod)
                || (event.getStartsAt().isBefore(startPeriod) && event.getEndsAt().isAfter(endPeriod))
                || (event.getStartsAt().isAfter(startPeriod) && event.getEndsAt().isBefore(endPeriod));
    }

    private void checkOverlappingEvent(Event event) {
        var eventsByPeriod = findEventByUserAndPeriod(event.getOwner(), event.getStartsAt(), event.getEndsAt());
        if (!eventsByPeriod.isEmpty()) throw new IllegalArgumentException("Cannot create overlapping event.");
    }


}
