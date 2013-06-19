package fr.tkeunebr.ic07.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtils {
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm");

	public static String format(Date d) {
		return sDateFormat.format(d);
	}
}
