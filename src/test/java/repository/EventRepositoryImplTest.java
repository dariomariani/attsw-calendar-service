package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import models.Event;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import repository.impl.EventRepositoryImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EventRepositoryImplTest {

    private EventRepositoryImpl eventRepository;

    @Mock
    private EntityManagerFactory entityManagerFactory;

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    @Mock
    private TypedQuery<Event> typedQuery;


    @Before
    public void setUp() {
        eventRepository = new EventRepositoryImpl(entityManagerFactory);
    }

    @Test
    public void testSave() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        UUID eventId = UUID.randomUUID();
        Event event = new Event(eventId);
        event.setName("Test Event");
        event.setStartsAt(LocalDateTime.now());
        event.setEndsAt(LocalDateTime.now().plusHours(1));

        UUID id = eventRepository.save(event);

        verify(transaction).begin();
        verify(entityManager, times(1)).persist(event);
        verify(transaction).commit();
        verify(entityManager).close();
        assertNotNull(id);
        assertEquals(eventId, id);
    }

    @Test
    public void testSaveRollbackOnException() {
        Event event = new Event();
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(transaction.isActive()).thenReturn(true);
        doThrow(RuntimeException.class).when(entityManager).persist(event);

        assertThrows(RuntimeException.class, () -> eventRepository.save(event));
        verify(entityManager).getTransaction();
        verify(entityManager).persist(event);
        verify(transaction).begin();
        verify(transaction).isActive();
        verify(transaction).rollback();
        verify(entityManager).close();
        verifyNoMoreInteractions(entityManager, transaction);
    }

    @Test
    public void testSaveRollbackOnSessionInactiveException() {
        // Arrange
        Event event = new Event();
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(transaction.isActive()).thenReturn(false);
        doThrow(RuntimeException.class).when(entityManager).persist(event);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> eventRepository.save(event));
        verify(entityManager).getTransaction();
        verify(entityManager).persist(event);
        verify(transaction).begin();
        verify(transaction).isActive();
        verify(transaction, times(0)).rollback();
        verify(entityManager).close();
        verifyNoMoreInteractions(entityManager, transaction);
    }

    @Test
    public void testFindById() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        UUID id = UUID.randomUUID();
        Event event = new Event(id);
        event.setName("Test Event");

        when(entityManager.find(Event.class, id)).thenReturn(event);

        Event eventFound = eventRepository.findById(id);

        verify(entityManager, times(1)).find(any(), any());
        verify(entityManager).close();
        assertNotNull(eventFound);
        assertEquals(eventFound.getId(), id);
    }

    @Test
    public void testFindAllThenCloseSession() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);

        List<Event> mockEntities = new ArrayList<>();
        mockEntities.add(new Event());
        mockEntities.add(new Event());

        when(typedQuery.getResultList()).thenReturn(mockEntities);
        when(entityManager.createQuery(anyString(), eq(Event.class))).thenReturn(typedQuery);

        eventRepository.findAll();

        verify(entityManager, times(1)).close();
    }


    @Test
    public void testFindAll() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(anyString(), eq(Event.class))).thenReturn(typedQuery);
        when(entityManager.getTransaction()).thenReturn(transaction);
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Event event1 = new Event(id1);
        event1.setName("Test Event 1");
        event1.setStartsAt(LocalDateTime.now());
        event1.setEndsAt(LocalDateTime.now().plusHours(1));
        eventRepository.save(event1);

        Event event2 = new Event(id2);
        event2.setName("Test Event 2");
        event2.setStartsAt(LocalDateTime.now().plusDays(1));
        event2.setEndsAt(LocalDateTime.now().plusDays(1).plusHours(2));
        eventRepository.save(event2);

        when(typedQuery.getResultList()).thenReturn(Arrays.asList(event1, event2));

        List<Event> allEvents = eventRepository.findAll();
        assertEquals(2, allEvents.size());
        assertEquals(id1, allEvents.get(0).getId());
        assertEquals(id2, allEvents.get(1).getId());
    }


}
