package repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import models.User;
import repository.impl.UserRepositoryImpl;

public class UserRepositoryImplTest {
	
	private UserRepositoryImpl userRepository;
	private EntityManagerFactory entityManagerFactory;
	
	@Before
	public void setUp() {
		entityManagerFactory = Persistence.createEntityManagerFactory("h2");
		userRepository = new UserRepositoryImpl(entityManagerFactory);
	}
	
	@After
	public void tearDown() {
		entityManagerFactory.close();
	}
	
	@Test
	public void testSave() {
		User user = new User();
		user.setUsername("johndoe");
		
		UUID id = userRepository.save(user);
		assertNotNull(id);
		assertEquals(user.getId(), id);
	}
	
	@Test
	public void testFindById() {
		User user = new User();
		user.setUsername("johndoe");
		
		UUID id = userRepository.save(user);
		User foundUser = userRepository.findById(id);
		
		assertNotNull(foundUser);
		assertEquals(user.getId(), foundUser.getId());
		assertEquals(user.getUsername(), foundUser.getUsername());
	}
	
	@Test
	public void testFindAll() {
		User user1 = new User();
		user1.setUsername("johndoe");
		UUID id1 = userRepository.save(user1);
		
		User user2 = new User();
		user2.setUsername("janedoe");
		UUID id2 = userRepository.save(user2);
		
		List<User> allUsers = userRepository.findAll();
		assertEquals(2, allUsers.size());
		assertEquals(id1, allUsers.get(0).getId());
		assertEquals(id2, allUsers.get(1).getId());
	}
}
