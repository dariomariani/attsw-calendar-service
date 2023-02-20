package services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import models.User;
import repository.impl.UserRepositoryImpl;

public class UserServiceTest {
	
	private UserService userService;
	
	@Mock
	private UserRepositoryImpl userRepository;
	
	@Before
	public void setup() {
		MockitoAnnotations.openMocks(this);
		this.userService = new UserService(userRepository);
	}

	@Test
    public void testFindAll() {
        List<User> userList = new ArrayList<>();
        userList.add(new User("John"));
        userList.add(new User("Jane"));
        when(userRepository.findAll()).thenReturn(userList);
        List<User> result = userService.findAll();
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testCreateUser() {
        User newUser = new User("John");
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        userService.createUser(newUser);
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    public void testCreateUserWithExistingUsername() {
        User existingUser = new User("John");
        User newUser = new User("John");
        List<User> userList = new ArrayList<User>();
        userList.add(existingUser);
        when(userRepository.findAll()).thenReturn(userList);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(newUser);
        });
        assertEquals("A User with the Username John already exists.", exception.getMessage());
        verify(userRepository, never()).save(newUser);
    }

}
