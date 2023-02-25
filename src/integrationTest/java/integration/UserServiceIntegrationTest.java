package integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.List;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import models.User;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import repository.impl.UserRepositoryImpl;
import services.UserService;

public class UserServiceIntegrationTest {

  private static EntityManagerFactory entityManagerFactory;
  private static UserService userService;

  @BeforeClass
  public static void setUp() {
    entityManagerFactory = Persistence.createEntityManagerFactory("h2");
    UserRepositoryImpl userRepository = new UserRepositoryImpl(entityManagerFactory);
    userService = new UserService(userRepository);
  }

  @AfterClass
  public static void tearDown() {
    entityManagerFactory.close();
  }

  @Test
  public void testCreateUser() {
    // Arrange
    User newUser = new User("johndoe");
    int initialSize = userService.findAll().size();

    // Act
    userService.createUser(newUser);
    List<User> users = userService.findAll();

    // Assert
    assertEquals(initialSize + 1, users.size());
    assertEquals(newUser, users.get(users.size() - 1));
  }

  @Test
  public void testCreateUserAlreadyExists() {
    // Arrange
    User newUser = new User("janedoe");
    userService.createUser(newUser);

    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, new ThrowingRunnable() {
      public void run() throws Throwable {
        userService.createUser(newUser);
      }
    });
    assertEquals("A User with the Username janedoe already exists.", exception.getMessage());
  }

}
