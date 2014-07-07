package de.uni_hamburg.informatik.mci.lineracalendar.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.text.format.Time;

public class Utility {
	

	public static Time setTimeZone(String timeZone) throws ParseException {
		Date sdf = new SimpleDateFormat("yyyy-M-dd", Locale.GERMANY)
				.parse(timeZone);
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf);
		Time time = new Time();
		int dayMonth = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		time.set(dayMonth, month, year);
		return time;

	}

	public static String getLabelDayOfMonth(int day) {
		String labelDay;
		String name = Integer.toString(day);
		if (name.length() == 1) {
			labelDay = "0" + name;
		} else {
			labelDay = name;
		}
		return labelDay;
	}

	

	public static int getNumberofDaysOfMonth(Time time, int month) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		time.set(1, month, year);
		int days = time.getActualMaximum(Time.MONTH_DAY);
		
		return days;
	}

	public static int getFirstDayOfWeek() {
		int startDay = Calendar.getInstance().getFirstDayOfWeek();
		return startDay;
	}

	public static boolean isSaturday(int column, int firstDayOfWeek) {
		return (firstDayOfWeek == Time.SUNDAY && column == 6)
				|| (firstDayOfWeek == Time.MONDAY && column == 5)
				|| (firstDayOfWeek == Time.SATURDAY && column == 0);
	}

	public static boolean isSunday(int column, int firstDayOfWeek) {
		return (firstDayOfWeek == Time.SUNDAY && column == 0)
				|| (firstDayOfWeek == Time.MONDAY && column == 6)
				|| (firstDayOfWeek == Time.SATURDAY && column == 1);
	}

}
