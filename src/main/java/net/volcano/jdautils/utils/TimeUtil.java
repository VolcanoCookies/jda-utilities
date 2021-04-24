package net.volcano.jdautils.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Date;

public class TimeUtil {
	
	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static String format(Instant instant) {
		return simpleDateFormat.format(Date.from(instant));
	}
	
	public static String format(OffsetDateTime dateTime) {
		return format(dateTime.toInstant());
	}
	
	public static String formatEpoch(Long epoch) {
		return simpleDateFormat.format(Date.from(Instant.ofEpochMilli(epoch)));
	}
	
}
