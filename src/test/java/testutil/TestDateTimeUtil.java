package testutil;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import utils.DateTimeUtil;
@RunWith(Parameterized.class)
public class TestDateTimeUtil {
	
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private LocalDateTime expectedDate;
	private String expectedFormat;
	private boolean expectedResult;
	
	
	
	public TestDateTimeUtil(LocalDateTime startDate, String expectedFormat,LocalDateTime endDate, LocalDateTime expectedDate, boolean expectedResult) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.expectedDate = expectedDate;
		this.expectedFormat = expectedFormat;
		this.expectedResult = expectedResult;
	}

	@Parameters()
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
			{LocalDateTime.of(2022, 1, 1, 9, 6, 0, 0), "09:06 01-01-2022",LocalDateTime.of(2022, 1, 1, 9, 16, 0, 0), LocalDateTime.of(2022, 1, 1, 9, 10, 0, 0), true},
			{LocalDateTime.of(2022, 1, 1, 9, 6, 0, 0), "09:06 01-01-2022",LocalDateTime.of(2022, 1, 1, 9, 16, 0, 0), LocalDateTime.of(1985, 1, 1, 9, 10, 0, 0), false},
			{LocalDateTime.of(2022, 1, 1, 9, 6, 0, 0), "09:06 01-01-2022",LocalDateTime.of(2022, 1, 1, 11, 16, 0, 0), LocalDateTime.of(2022, 1, 1, 10, 10, 0, 0), true},
			{LocalDateTime.of(2022, 1, 1, 9, 6, 0, 0), "09:06 01-01-2022",LocalDateTime.of(2022, 1, 5, 9, 16, 0, 0), LocalDateTime.of(2022, 1, 3, 9, 10, 0, 0), true},
			{LocalDateTime.of(2022, 1, 1, 9, 6, 0, 0), "09:06 01-01-2022",LocalDateTime.of(2022, 3, 1, 9, 16, 0, 0), LocalDateTime.of(2022, 2, 1, 9, 10, 0, 0), true},
			{LocalDateTime.of(2022, 10, 10, 19, 45, 0, 0), "19:45 10-10-2022",LocalDateTime.of(2022, 12, 1, 9, 16, 0, 0), LocalDateTime.of(2022, 11, 1, 9, 10, 0, 0), true},
	 	});
	 }

	@Test
	public void testFormatDatetimeHasDayMonthsYearMinutesSecondsCorrect() {
		assertEquals(DateTimeUtil.formatLocalDateTime(startDate), expectedFormat);
	}
	
	@Test
	public void testCheckDateIsBetweenTwoDatesWhenIsBetween() {
		assertEquals(expectedResult, DateTimeUtil.isBetween(startDate, endDate, expectedDate));
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testWhenIstantiateUtilityClassThrowsException() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException {
		Constructor<DateTimeUtil> c = DateTimeUtil.class.getDeclaredConstructor();
		c.setAccessible(true);
		try {
	        c.newInstance();
	    } catch (InvocationTargetException e) {
	        throw (UnsupportedOperationException) e.getTargetException();
	    }
	}

}
