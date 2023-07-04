package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import repository.impl.UserRepositoryImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryImplTest {

    private UserRepositoryImpl userRepository;

    @Mock
    private EntityManagerFactory entityManagerFactory;

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    @Mock
    private TypedQuery<User> typedQuery;

    @Before
    public void setUp() {
        userRepository = new UserRepositoryImpl(entityManagerFactory);
    }

    @After
    public void tearDown() {
        entityManagerFactory.close();
    }

    @Test
    public void testSave() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);

        UUID userId = UUID.randomUUID();

        User user = new User(userId, "johndoe");

        UUID id = userRepository.save(user);
        assertNotNull(id);
        assertEquals(userId, id);
    }

    @Test
    public void testFindById() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);

        UUID userId = UUID.randomUUID();
        User user = new User(userId, "johndoe");

        when(entityManager.find(User.class, userId)).thenReturn(user);

        User foundUser = userRepository.findById(userId);

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getUsername(), foundUser.getUsername());
        verify(entityManager).close();
    }

    @Test
    public void testFindAllThenCloseSession() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);

        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());

        when(typedQuery.getResultList()).thenReturn(users);
        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(typedQuery);

        userRepository.findAll();

        verify(entityManager, times(1)).close();
    }

    @Test
    public void testSaveRollbackOnException() {
        User user = new User();
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(transaction.isActive()).thenReturn(true);
        doThrow(RuntimeException.class).when(entityManager).persist(user);

        assertThrows(RuntimeException.class, () -> userRepository.save(user));
        verify(entityManager).getTransaction();
        verify(entityManager).persist(user);
        verify(transaction).begin();
        verify(transaction).isActive();
        verify(transaction).rollback();
        verify(entityManager).close();
        verifyNoMoreInteractions(entityManager, transaction);
    }

    @Test
    public void testFindAll() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(typedQuery);
        when(entityManager.getTransaction()).thenReturn(transaction);
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        User user1 = new User(id1, "johndoe");
        userRepository.save(user1);

        User user2 = new User(id2, "janedoe");
        userRepository.save(user2);

        when(typedQuery.getResultList()).thenReturn(Arrays.asList(user1, user2));

        List<User> allUsers = userRepository.findAll();
        assertEquals(2, allUsers.size());
        assertEquals(id1, allUsers.get(0).getId());
        assertEquals(id2, allUsers.get(1).getId());
    }
}
