package repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import models.Event;
import repository.impl.EventRepositoryImpl;

@RunWith(MockitoJUnitRunner.class)
public class EventRepositoryImplTest {

	private EventRepositoryImpl eventRepository;
	
	private EntityManagerFactory entityManagerFactory;
	
	@Mock
	private EntityManagerFactory entityManagerFactoryMocked;
	
	@Mock
	private EntityManager entityManager;
	
	@Mock
	private EntityTransaction transaction;
	
	@Mock
	private TypedQuery<Event> typedQuery;
	
	private EventRepositoryImpl eventRepositoryMocked;

	@Before
	public void setUp() {
		entityManagerFactory = Persistence.createEntityManagerFactory("h2");
		eventRepository = new EventRepositoryImpl(entityManagerFactory);
		eventRepositoryMocked = new EventRepositoryImpl(entityManagerFactoryMocked);
	}

	@After
	public void tearDown() {
		entityManagerFactory.close();
	}

	@Test
	public void testSave() {
		when(entityManagerFactoryMocked.createEntityManager()).thenReturn(entityManager);
		when(entityManager.getTransaction()).thenReturn(transaction);
		UUID eventId = UUID.randomUUID();
;		Event event = new Event(eventId);
		event.setName("Test Event");
		event.setStartsAt(LocalDateTime.now());
		event.setEndsAt(LocalDateTime.now().plusHours(1));

		UUID id = eventRepositoryMocked.save(event);
		verify(entityManager).close();
		assertNotNull(id);
		assertEquals(eventId, id);
	}

	@Test
	public void testFindById() {
		Event event = new Event();
		event.setName("Test Event");
		event.setStartsAt(LocalDateTime.of(2023, 3, 13, 18, 0));
		event.setEndsAt(LocalDateTime.of(2023, 3, 13, 18, 0));

		UUID id = eventRepository.save(event);
		Event foundEvent = eventRepository.findById(id);

		assertNotNull(foundEvent);
		assertEquals(event.getId(), foundEvent.getId());
		assertEquals(event.getName(), foundEvent.getName());
		assertEquals(event.getStartsAt(), foundEvent.getStartsAt());
		assertEquals(event.getEndsAt(), foundEvent.getEndsAt());
	}
	
	@Test
	public void testFindByIdThenCloseSession() {
	    UUID id = UUID.randomUUID();
	    Event event = new Event(id);

	    when(entityManagerFactoryMocked.createEntityManager()).thenReturn(entityManager);
	    when(entityManager.find(Event.class, id)).thenReturn(event);
	    eventRepositoryMocked.findById(id);
	    verify(entityManager, times(1)).close();
	}
	
	@Test
	public void testFindAllThenCloseSession() {

	    // Mock the behavior of entityManagerFactory and entityManager
	    when(entityManagerFactoryMocked.createEntityManager()).thenReturn(entityManager);

	    // Create a mock list of entities
	    List<Event> mockEntities = new ArrayList<>();
	    mockEntities.add(new Event());
	    mockEntities.add(new Event());

	    // Mock the behavior of the TypedQuery object
	    when(typedQuery.getResultList()).thenReturn(mockEntities);
	    when(entityManager.createQuery(anyString(), eq(Event.class))).thenReturn(typedQuery);

	    // Call the method under test
	    eventRepositoryMocked.findAll();

	    // Verify that entityManager.close() is called exactly once
	    verify(entityManager, times(1)).close();
	}
	

	@Test
	public void testFindAll() {
		Event event1 = new Event();
		event1.setName("Test Event 1");
		event1.setStartsAt(LocalDateTime.now());
		event1.setEndsAt(LocalDateTime.now().plusHours(1));
		UUID id1 = eventRepository.save(event1);

		Event event2 = new Event();
		event2.setName("Test Event 2");
		event2.setStartsAt(LocalDateTime.now().plusDays(1));
		event2.setEndsAt(LocalDateTime.now().plusDays(1).plusHours(2));
		UUID id2 = eventRepository.save(event2);

		List<Event> allEvents = eventRepository.findAll();
		assertEquals(2, allEvents.size());
		assertEquals(id1, allEvents.get(0).getId());
		assertEquals(id2, allEvents.get(1).getId());
	}
	
	@Test
	public void testSaveRollbackOnException() {
		// Arrange
		Event event = new Event();
		when(entityManagerFactoryMocked.createEntityManager()).thenReturn(entityManager);
		when(entityManager.getTransaction()).thenReturn(transaction);
		when(transaction.isActive()).thenReturn(true);
		doThrow(RuntimeException.class).when(entityManager).persist(event);

		// Act & Assert
		assertThrows(RuntimeException.class, () -> eventRepositoryMocked.save(event));
		verify(entityManager).getTransaction();
		verify(entityManager).persist(event);
		verify(transaction).begin();
		verify(transaction).isActive();
		verify(transaction).rollback();
		verify(entityManager).close();
		verifyNoMoreInteractions(entityManager, transaction);
	}
}
