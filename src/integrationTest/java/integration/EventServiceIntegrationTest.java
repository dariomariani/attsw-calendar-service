package integration;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import models.Event;
import models.User;
import repository.impl.EventRepositoryImpl;
import services.EventService;

public class EventServiceIntegrationTest {
	
	private static final Logger logger = Logger.getLogger(EventServiceIntegrationTest.class.getName());
    private static EntityManagerFactory entityManagerFactory;
    private static EventService eventService;

    @BeforeClass
    public static void setUp() {
        String dbProvider = System.getProperty("dbprovider");
    	if (dbProvider == null || dbProvider.isEmpty()) {
    		logger.info("!!! No dbprovider found");
    		dbProvider = "h2";
    	}
    	logger.info("Running EventServiceIntegrationTest with DB: " + dbProvider + " ...");
        entityManagerFactory = Persistence.createEntityManagerFactory(dbProvider);
        
        // Create the repository and service instances
        EventRepositoryImpl eventRepository = new EventRepositoryImpl(entityManagerFactory);
        eventService = new EventService(eventRepository);
    }


    @AfterClass
    public static void tearDown() {
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
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
    }
}
