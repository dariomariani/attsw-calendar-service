package data;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import models.User;

@RunWith(Parameterized.class)
public class UserTest {
	
	private final Object input1;
	private final Object input2;
	private final boolean expected;
	
	public UserTest(Object input1, Object input2, boolean expected) {
		this.input1 = input1;
		this.input2 = input2;
		this.expected = expected;
	}

	@Parameters()
	public static Collection<Object[]> data() {
		var user1 = new User("John");
		var user2 = new User("Jane");
		return Arrays.asList(new Object[][] {
			{user1, user1, true},
			{user1, user2, false},
			{user1, null, false},
			{user1, "User", false},
	 	});
	 }
	 
	
	@Test
	public void testEqualsObjectPair() {
		assertEquals(expected, input1.equals(input2));
	}
	
	
}
