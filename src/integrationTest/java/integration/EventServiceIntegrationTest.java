package integration;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import models.Event;
import models.User;
import repository.impl.EventRepositoryImpl;
import services.EventService;

public class EventServiceIntegrationTest {

    private EntityManagerFactory entityManagerFactory;
    private EventRepositoryImpl eventRepository;
    private EventService eventService;

    @Before
    public void setup() {
        // Create an in-memory H2 database
        entityManagerFactory = Persistence.createEntityManagerFactory("h2");
        
        // Create the repository and service instances
        eventRepository = new EventRepositoryImpl(entityManagerFactory);
        eventService = new EventService(eventRepository);
    }

    @After
    public void cleanup() {
        entityManagerFactory.close();
    }

    @Test
    public void testFindAll() {
        Event event1 = new Event("Event 1", new User("User 1"), LocalDateTime.of(2023, 3, 13, 18, 0), LocalDateTime.of(2023, 3, 13, 19, 0));
        Event event2 = new Event("Event 2", new User("User 2"), LocalDateTime.of(2023, 3, 13, 9, 0), LocalDateTime.of(2023, 3, 13, 10, 0));
        eventService.createEvent(event1);
        eventService.createEvent(event2);

        // Call the service method
        List<Event> events = eventService.findAll();

        // Verify the results
        assertEquals(2, events.size());
        assertFalse(events.contains(event1));
        assertTrue(events.contains(event2));
    }
}
