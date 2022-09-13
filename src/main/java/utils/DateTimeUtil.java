package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
	
	public static String formatLocalDateTime(LocalDateTime dateTime) {
		var formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
		return dateTime.format(formatter);
	}

	public static boolean isBetween(LocalDateTime startDate, LocalDateTime endDate, LocalDateTime testDate) {
		return !(testDate.isBefore(startDate) || testDate.isAfter(endDate));
	}

}
