package data;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import testdataset.TestUserDataset;

@RunWith(Parameterized.class)
public class UserTest {
	
	private Object input1;
	private Object input2;
	private boolean expected;
	
	public UserTest(Object input1, Object input2, boolean expected) {
		this.input1 = input1;
		this.input2 = input2;
		this.expected = expected;
	}

	@Parameters()
	public static Collection<Object[]> data() {
		var user1 = new User(TestUserDataset.USERNAME1);
		var user2 = new User(TestUserDataset.USERNAME2);
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
