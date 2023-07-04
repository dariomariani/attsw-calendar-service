package services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import models.Event;
import models.User;
import repository.impl.EventRepositoryImpl;

@RunWith(MockitoJUnitRunner.class)
public class EventServiceTest {

    private User standardUser1;
    private User standardUser2;
    private EventService eventService;
    @Mock
    private EventRepositoryImpl eventRepository;
    private AutoCloseable closeable;

    @Before
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        standardUser1 = new User(id1, "John");
        standardUser2 = new User(id2, "Jane");
        eventService = new EventService(eventRepository);
    }

    @After
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    public void testCreateEventWhenEventNameIsNullShouldThrowIllegalArgumentException() {
        var newEvent = new Event(null, standardUser1, LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        assertThrows("EventName cannot be null nor blank.", IllegalArgumentException.class, () -> eventService.createEvent(newEvent));
    }

    @Test
    public void testCreateEventWhenEventNameIsEmptyShouldThrowIllegalArgumentException() {
        var newEvent2 = new Event("", standardUser1, LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        assertThrows("EventName cannot be null nor blank.", IllegalArgumentException.class, () -> eventService.createEvent(newEvent2));
    }

    @Test
    public void testCreateEventWhenEventNameIsBlankShouldThrowIllegalArgumentException() {
        var newEvent3 = new Event(" ", standardUser1, LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        assertThrows("EventName cannot be null nor blank.", IllegalArgumentException.class, () -> eventService.createEvent(newEvent3));
    }

    @Test
    public void testCreateEventWhenStartDateIsNullShouldThrowIllegalArgumentException() {
        var newEvent = new Event("Event without Start date", standardUser1, null, null);
        assertThrows("StartsAt cannot be null.", IllegalArgumentException.class, () -> eventService.createEvent(newEvent));
    }

    @Test
    public void testCreateEventWhenOwnerIsNullShouldThrowIllegalArgumentException() {
        var newEvent = new Event("Event Without Owner", null, LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        assertThrows("Owner cannot be null.", IllegalArgumentException.class, () -> eventService.createEvent(newEvent));
    }

    @Test
    public void testCreateEventWhenEndsAtIsPriorStartsAtThrowIllegalArgumentException() {
        var newEvent = new Event("Event with negative timespan", standardUser1, LocalDateTime.now(), LocalDateTime.now().minusHours(2));
        assertThrows("EndsAt must be after StartsAt.", IllegalArgumentException.class, () -> eventService.createEvent(newEvent));
    }

    @Test
    public void testCreateEventWhenEndDateIsNullShouldThrowIllegalArgumentException() {
        var newEvent = new Event("Event without End date", standardUser1, LocalDateTime.now(), null);
        assertThrows("EndsAt cannot be null.", IllegalArgumentException.class, () -> eventService.createEvent(newEvent));
    }

    @Test
    public void testFindAll() {
        List<Event> expectedEvents = Arrays.asList(new Event("Event 1", standardUser1, LocalDateTime.now().plusHours(1), LocalDateTime.now()), new Event("Event 2", standardUser2, LocalDateTime.now().plusHours(2), LocalDateTime.now()));
        when(eventRepository.findAll()).thenReturn(expectedEvents);

        List<Event> actualEvents = eventService.findAll();

        assertEquals(expectedEvents, actualEvents);
    }

    @Test
    public void testFindEventByUserAndPeriod() {
        UUID userId = UUID.randomUUID();
        User owner = new User(userId, "user1");
        LocalDateTime startPeriod = LocalDateTime.now();
        LocalDateTime endPeriod = startPeriod.plusHours(2);

        List<Event> allEvents = Arrays.asList(new Event("Event 1", owner, endPeriod.plusHours(1), startPeriod.minusHours(1)), new Event("Event 2", owner, endPeriod.minusHours(1), startPeriod.minusHours(2)), new Event("Event 3", owner, endPeriod.plusHours(2), endPeriod.plusHours(1)));
        when(eventRepository.findAll()).thenReturn(allEvents);

        List<Event> expectedEvents = Arrays.asList(allEvents.get(0), allEvents.get(1));
        List<Event> actualEvents = eventService.findEventByUserAndPeriod(owner, startPeriod, endPeriod);

        assertEquals(expectedEvents, actualEvents);
    }

    @Test
    public void testFindEventById() {
        UUID eventId = UUID.randomUUID();
        Event expectedEvent = new Event("Event 1", standardUser1, LocalDateTime.now().plusHours(1), LocalDateTime.now());
        when(eventRepository.findById(eventId)).thenReturn(expectedEvent);

        Event actualEvent = eventService.findEventById(eventId);

        assertEquals(expectedEvent, actualEvent);
    }

    @Test
    public void testCreateEvent() {
        Event newEvent = new Event("New Event", standardUser1, LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        eventService.createEvent(newEvent);

        verify(eventRepository, times(1)).save(newEvent);
    }

    @Test
    public void testUpdateEvent() {
        // create a test event
        Event event = new Event("Test Event", standardUser1, LocalDateTime.of(2023, 2, 22, 10, 0), LocalDateTime.of(2023, 2, 22, 11, 0));
        // update the test event
        Event updatedEvent = new Event("Updated Event", standardUser2, LocalDateTime.of(2023, 2, 23, 11, 0), LocalDateTime.of(2023, 2, 23, 12, 0));
        // add the test event to the repository
        when(eventRepository.findById(updatedEvent.getId())).thenReturn(event);
        eventService.updateEvent(updatedEvent);
        assertEquals("Updated Event", event.getName());
        assertEquals(LocalDateTime.of(2023, 2, 23, 11, 0), event.getStartsAt());
        assertEquals(LocalDateTime.of(2023, 2, 23, 12, 0), event.getEndsAt());
        // test case where the event is not found
        // add the test event to the repository
        when(eventRepository.findById(updatedEvent.getId())).thenReturn(null);
        assertThrows("Event not found", IllegalArgumentException.class, () -> eventService.updateEvent(updatedEvent));
    }

    @Test
    public void testUpdateInvalidEventThrowsException() {
        Event event = new Event("Test Event", standardUser1, LocalDateTime.of(2023, 2, 22, 10, 0), LocalDateTime.of(2023, 2, 22, 11, 0));
        Event existingEvent = new Event("Existing Event", standardUser1, LocalDateTime.of(2023, 2, 23, 10, 0), LocalDateTime.of(2023, 2, 23, 11, 0));
        Event updatedEvent = new Event("Test Event", standardUser1, LocalDateTime.of(2023, 2, 23, 10, 0), LocalDateTime.of(2023, 2, 23, 11, 0));

        when(eventRepository.findById(event.getId())).thenReturn(event);
        when(eventRepository.findAll()).thenReturn(List.of(existingEvent));

        assertThrows("Event not found", IllegalArgumentException.class, () -> eventService.updateEvent(updatedEvent));
    }

    @Test
    public void testFindEventByUserAndPeriodWithNoOverlap() {
        User user1 = new User(UUID.randomUUID(), "user1");
        User user2 = new User(UUID.randomUUID(), "user2");
        LocalDateTime start1 = LocalDateTime.of(2023, 2, 21, 12, 0, 0);
        LocalDateTime end1 = LocalDateTime.of(2023, 2, 21, 14, 0, 0);
        LocalDateTime start2 = LocalDateTime.of(2023, 2, 22, 12, 0, 0);
        LocalDateTime end2 = LocalDateTime.of(2023, 2, 22, 14, 0, 0);
        Event event1 = new Event("Event 1", user1, start1, end1);
        Event event2 = new Event("Event 2", user2, start2, end2);
        when(eventRepository.findAll()).thenReturn(Arrays.asList(event1, event2));

        List<Event> events = eventService.findEventByUserAndPeriod(user1, start2, end2);

        assertEquals(0, events.size());
    }

    @Test
    public void testFindEventByUserAndPeriodWithStartOverlap() {
        User user1 = new User(UUID.randomUUID(), "user1");
        LocalDateTime start1 = LocalDateTime.of(2023, 2, 21, 12, 0, 0);
        LocalDateTime end1 = LocalDateTime.of(2023, 2, 21, 14, 0, 0);
        LocalDateTime start2 = LocalDateTime.of(2023, 2, 22, 12, 0, 0);
        LocalDateTime end2 = LocalDateTime.of(2023, 2, 22, 14, 0, 0);
        Event event1 = new Event("Event 1", user1, start1, end1);
        Event event2 = new Event("Event 2", user1, start2, end2);
        Mockito.when(eventRepository.findAll()).thenReturn(Arrays.asList(event1, event2));

        List<Event> events = eventService.findEventByUserAndPeriod(user1, start1.minusHours(1), start2);

        assertEquals(2, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
    }

    @Test
    public void testFindEventByUserAndPeriodWithEndOverlap() {
        User user1 = new User(UUID.randomUUID(), "user1");
        LocalDateTime start1 = LocalDateTime.of(2023, 2, 21, 12, 0, 0);
        LocalDateTime end1 = LocalDateTime.of(2023, 2, 21, 14, 0, 0);
        LocalDateTime start2 = LocalDateTime.of(2023, 2, 22, 12, 0, 0);
        LocalDateTime end2 = LocalDateTime.of(2023, 2, 22, 14, 0, 0);
        Event event1 = new Event("Event 1", user1, start1, end1);
        Event event2 = new Event("Event 2", user1, start2, end2);
        Mockito.when(eventRepository.findAll()).thenReturn(Arrays.asList(event1, event2));

        List<Event> events = eventService.findEventByUserAndPeriod(user1, start1, start2.plusHours(1));

        assertEquals(2, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
    }

    @Test
    public void testCheckOverlappingEventThrowsException() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "testuser");
        LocalDateTime start1 = LocalDateTime.of(2023, 3, 1, 10, 0);
        LocalDateTime end1 = LocalDateTime.of(2023, 3, 1, 12, 0);
        LocalDateTime start2 = LocalDateTime.of(2023, 3, 1, 11, 0);
        LocalDateTime end2 = LocalDateTime.of(2023, 3, 1, 13, 0);
        Event event1 = new Event("Event 1", user, start1, end1);
        Event event2 = new Event("Event 2", user, start2, end2);

        when(eventRepository.findAll()).thenReturn(Arrays.asList(event1, event2));

        LocalDateTime start3 = LocalDateTime.of(2023, 3, 1, 11, 30);
        LocalDateTime end3 = LocalDateTime.of(2023, 3, 1, 13, 30);
        Event newEvent = new Event("Event 3", user, start3, end3);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> eventService.createEvent(newEvent));
        assertEquals("Cannot create overlapping event.", exception.getMessage());
    }

}
