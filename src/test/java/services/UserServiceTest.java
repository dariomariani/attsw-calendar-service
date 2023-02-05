package services;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import models.User;
import testdataset.TestUserDataset;

public class UserServiceTest {
	
	private UserService userService;
	
	@Before
	public void setup() {
		var userRepository = new ArrayList<User>();
		this.userService = new UserService(userRepository);
	}

	@Test
	public void testCreateUserWithSameUsernameAsAnExistingThrowsIllegalArgumentException() {
		var newUser1 = new User(TestUserDataset.USERNAME1);
		userService.createUser(newUser1);
		var newUser2 = new User(TestUserDataset.USERNAME1);
		assertThrows(String.format("A User with the Username {0} already exists.", TestUserDataset.USERNAME1), IllegalArgumentException.class, () -> userService.createUser(newUser2));
	}
	
	@Test
	public void testCreateUserWhenRepositoryIsEmptyContainsOneElement() {
		var newUser = new User(TestUserDataset.USERNAME1);
		userService.createUser(newUser);
		assertEquals(1, userService.findAll().size());
	}

}
