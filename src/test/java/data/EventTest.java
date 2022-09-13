package data;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import testdataset.TestUserDataset;

public class EventTest {
	
	private User standardUser;
	
	@Before
	public void setup() {
		this.standardUser = new User(TestUserDataset.USERNAME1);
	}

	@Test
	public void testCreateMultipleNewEventsHaveDifferentIds() {
		var newEvent1 = new Event("Good Event", standardUser, LocalDateTime.now(), LocalDateTime.now().plusHours(2));
		var newEvent2 = new Event("Good Event2", standardUser, LocalDateTime.now(), LocalDateTime.now().plusHours(2));
		assertNotEquals(newEvent1.getId(), newEvent2.getId());
	}
	
	@Test
	public void testEqualsWhenSameObject() {
		var newEvent1 = new Event("Good Event", standardUser, LocalDateTime.now(), LocalDateTime.now().plusHours(2));
		assertEquals(true, newEvent1.equals(newEvent1));
	}
	
	@Test
	public void testEqualsWhenDifferentClass() {
		var newEvent1 = new Event("Good Event", standardUser, LocalDateTime.now(), LocalDateTime.now().plusHours(2));
		assertEquals(false, newEvent1.equals(new Object()));
	}
	
	@Test
	public void testEqualsWhenCompareWithNull() {
		var newEvent1 = new Event("Good Event", standardUser, LocalDateTime.now(), LocalDateTime.now().plusHours(2));
		assertEquals(false, newEvent1.equals(null));
	}

}
