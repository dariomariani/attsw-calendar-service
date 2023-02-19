package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtil {

	private DateTimeUtil() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}
	
	public static String formatLocalDateTime(LocalDateTime dateTime) {
		var formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
		return dateTime.format(formatter);
	}
	
	public static LocalDateTime parseLocalDateTime(String dateTime) {
		var formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
		return LocalDateTime.parse(dateTime, formatter);
	}

	public static boolean isBetween(LocalDateTime startDate, LocalDateTime endDate, LocalDateTime testDate) {
		return !(testDate.isBefore(startDate) || testDate.isAfter(endDate));
	}

}
