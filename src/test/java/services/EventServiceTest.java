package services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import models.Event;
import models.User;
import testdataset.TestUserDataset;

public class EventServiceTest {
	
	private User standardUser1;
	private User standardUser2;
	private EventService eventService;
	
	@Before
	public void setup() {
		this.standardUser1 = new User(TestUserDataset.USERNAME1);
		this.standardUser2 = new User(TestUserDataset.USERNAME2);
		var eventRepository = new ArrayList<Event>();
		this.eventService = new EventService(eventRepository);
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
	public void testCreateEventWhenRepositoryIsEmptyContainsOneElement() {
		var newEvent = new Event("Good Event", standardUser1, LocalDateTime.now(), LocalDateTime.now().plusHours(2));
		eventService.createEvent(newEvent);
		assertEquals(1, eventService.findAll().size());
	}
	
	@Test
	public void testFindEventsByOwnerAndPeriodQuery() {
		//Arrange
		var firstEvent = new Event("Amazing Event", standardUser1, LocalDateTime.now(), LocalDateTime.now().plusHours(4));
		var secondEvent = new Event("Another Amazing Event", standardUser1, LocalDateTime.now().plusHours(6), LocalDateTime.now().plusHours(8));
		var thirdEvent = new Event("Another Amazing Event", standardUser2, LocalDateTime.now().plusHours(6), LocalDateTime.now().plusHours(8));
		var fourthEvent = new Event("Useless Event", standardUser1, LocalDateTime.now().plusHours(9), LocalDateTime.now().plusHours(10)); 
		eventService.createEvent(firstEvent);
		eventService.createEvent(secondEvent);
		eventService.createEvent(thirdEvent);
		eventService.createEvent(fourthEvent);
		//Act
		var eventQueryResult = eventService.findEventByUserAndPeriod(standardUser1, LocalDateTime.now(), LocalDateTime.now().plusHours(7));
		//Assert
		assertTrue(eventQueryResult.contains(firstEvent));
		assertTrue(eventQueryResult.contains(secondEvent));
		assertFalse(eventQueryResult.contains(thirdEvent));
		assertFalse(eventQueryResult.contains(fourthEvent));
	}
	
	@Test
	public void testCreateEventWhenIsOverlappingAnotherEventThrowIllegalArgumentException() {
		var firstEvent = new Event("Good Event", standardUser1, LocalDateTime.now(), LocalDateTime.now().plusHours(4));
		eventService.createEvent(firstEvent);
		var overlappingEvent = new Event("Overlapping Event", standardUser1, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(4));
		assertThrows("Cannot create overlapping event.", IllegalArgumentException.class, () -> eventService.createEvent(overlappingEvent));
	}
	
	@Test
	public void testFindEventById() {
		var event = new Event("Soccer Match", standardUser1, LocalDateTime.now(), LocalDateTime.now().plusHours(4));
		eventService.createEvent(event);
		assertEquals(event, eventService.findEventById(event.getId()));
	}
	
	@Test
	public void testUpdateEventNameOfExistingEvent() {
		//Arrange
		var event = new Event("Soccer Match", standardUser1, LocalDateTime.now(), LocalDateTime.now().plusHours(4));
		eventService.createEvent(event);
		var updatedEvent = Mockito.mock(Event.class);
		when(updatedEvent.getId()).thenReturn(event.getId());
		when(updatedEvent.getName()).thenReturn("Wife Birthday");
		//Act
		eventService.updateEvent(updatedEvent);
		//Assert
		assertEquals(updatedEvent.getName(), event.getName());
	}
	
	@Test
	public void testUpdateEventNameEmptyOfExistingEvent() {
		//Arrange
		var event = new Event("Soccer Match", standardUser1, LocalDateTime.now(), LocalDateTime.now().plusHours(4));
		eventService.createEvent(event);
		var updatedEvent = Mockito.mock(Event.class);
		when(updatedEvent.getId()).thenReturn(event.getId());
		when(updatedEvent.getName()).thenReturn("");
		//Act & Assert
		assertThrows("EventName cannot be null nor empty nor blank.", IllegalArgumentException.class, () -> eventService.updateEvent(updatedEvent));
	}
	
	@Test
	public void testUpdateStartTimeOfExistingEvent() {
		//Arrange
		var startTime = LocalDateTime.now();
		var event = new Event("Soccer Match", standardUser1, startTime, LocalDateTime.now().plusHours(4));
		eventService.createEvent(event);
		var updatedEvent = Mockito.mock(Event.class);
		when(updatedEvent.getId()).thenReturn(event.getId());
		when(updatedEvent.getStartsAt()).thenReturn(startTime.plusHours(2));
		when(updatedEvent.getName()).thenReturn(event.getName());
		//Act
		eventService.updateEvent(updatedEvent);
		//Assert
		assertEquals(updatedEvent.getStartsAt(), event.getStartsAt());
	}
	
	@Test
	public void testUpdateEndTimeOfExistingEvent() {
		//Arrange
		var startTime = LocalDateTime.now();
		var endTime = startTime.plusHours(4);
		var event = new Event("Soccer Match", standardUser1, startTime, endTime);
		eventService.createEvent(event);
		var updatedEvent = Mockito.mock(Event.class);
		when(updatedEvent.getId()).thenReturn(event.getId());
		when(updatedEvent.getEndsAt()).thenReturn(endTime.plusHours(2));
		when(updatedEvent.getName()).thenReturn(event.getName());
		//Act
		eventService.updateEvent(updatedEvent);
		//Assert
		assertEquals(updatedEvent.getEndsAt(), event.getEndsAt());
	}

}
