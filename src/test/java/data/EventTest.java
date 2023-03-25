package data;

import models.Event;
import models.User;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class EventTest {
	
	private User standardUser;
	
	@Before
	public void setup() {
		this.standardUser = new User("John");
	}
	
	@Test
	public void testEqualsWhenSameObject() {
		var newEvent1 = new Event("Good Event", standardUser, LocalDateTime.now(), LocalDateTime.now().plusHours(2));
		assertEquals(newEvent1, newEvent1);
	}
	
	@Test
	public void testEqualsWhenDifferentClass() {
		var newEvent1 = new Event("Good Event", standardUser, LocalDateTime.now(), LocalDateTime.now().plusHours(2));
		assertNotEquals(newEvent1, new Object());
	}
	
	@Test
	public void testEqualsWhenCompareWithNull() {
		var newEvent1 = new Event("Good Event", standardUser, LocalDateTime.now(), LocalDateTime.now().plusHours(2));
		assertNotEquals(null, newEvent1);
	}

}
