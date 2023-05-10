package com.laundrysystem.backendapi.utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Formatting {

	public static Timestamp getCurTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}
	
	public static String timestampToDateStr(Timestamp timestamp) {
		Calendar calendar = getCalendarFromTimestamp(timestamp);
		
		String dayOfMonth = formatTimeComponent(calendar.get(Calendar.DAY_OF_MONTH));
		String month = formatTimeComponent(calendar.get(Calendar.MONTH) + 1);
		String year = formatTimeComponent(calendar.get(Calendar.YEAR));
		
		String minute = formatTimeComponent(calendar.get(Calendar.MINUTE));
		String hour = formatTimeComponent(calendar.get(Calendar.HOUR_OF_DAY));
		
		return dayOfMonth + SolutionConstants.DATE_DELIMITER 
			+ month + SolutionConstants.DATE_DELIMITER + year
			+ SolutionConstants.WHITESPACE + hour + SolutionConstants.TIME_DELIMITER + minute;
	}
	
	public static Timestamp curTimestampToCurTimeslot() {
		Timestamp curTimestamp = new Timestamp(System.currentTimeMillis());
		List<Integer> ymdhmTime = getYMDHMtime(curTimestamp);
		
		Calendar calendar = Calendar.getInstance((TimeZone.getTimeZone("UTC")));
		calendar.set(Calendar.YEAR, ymdhmTime.get(0).intValue());
		calendar.set(Calendar.MONTH, ymdhmTime.get(1).intValue() - 1);
		calendar.set(Calendar.DAY_OF_MONTH, ymdhmTime.get(2).intValue());
		calendar.set(Calendar.HOUR_OF_DAY, ymdhmTime.get(3).intValue());
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return new Timestamp(calendar.getTimeInMillis());
	}
	
	public static String getDayMonthYearFromTimestamp(Timestamp timestamp) {
		Calendar calendar = getCalendarFromTimestamp(timestamp);
		
		String dayOfMonth = formatTimeComponent(calendar.get(Calendar.DAY_OF_MONTH));
		String month = formatTimeComponent(calendar.get(Calendar.MONTH) + 1);
		String year = formatTimeComponent(calendar.get(Calendar.YEAR));
	
		return String.format("%s/%s/%s", dayOfMonth, month, year);
	}
	
	private static List<Integer> getYMDHMtime(Timestamp timestamp) {
		Date date = new Date(timestamp.getTime());
		Calendar calendar = Calendar.getInstance((TimeZone.getTimeZone("UTC")));
		calendar.setTime(date);
		
		List<Integer> time = new ArrayList<>();
		time.add(Integer.valueOf(calendar.get(Calendar.YEAR)));
		time.add(Integer.valueOf(calendar.get(Calendar.MONTH) + 1));
		time.add(Integer.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
		time.add(Integer.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
		time.add(Integer.valueOf(calendar.get(Calendar.MINUTE)));
		
		return time;
	}
	
	public static boolean isBookingTimeslot(Timestamp timestamp) {
		Calendar calendar = getCalendarFromTimestamp(timestamp);
		
		return
			calendar.get(Calendar.MINUTE) == 0 &&
			calendar.get(Calendar.SECOND) == 0 &&
			calendar.get(Calendar.MILLISECOND) == 0;
	}
	
	public static boolean isDailyBookingTimeslot(Timestamp timestamp) {
		Calendar calendar = getCalendarFromTimestamp(timestamp);
		return
			// calendar.get(Calendar.HOUR_OF_DAY) == 0 &&	NOTE: commented out due to Timezone conflicts
			calendar.get(Calendar.MINUTE) == 0 &&
			calendar.get(Calendar.SECOND) == 0 &&
			calendar.get(Calendar.MILLISECOND) == 0;
	}
	
	public static boolean isTimestampToday(Timestamp timestamp) {
		Timestamp curTimestamp = new Timestamp(System.currentTimeMillis());		
		Calendar calendarCur = getCalendarFromTimestamp(curTimestamp);
		Calendar calendarTs = getCalendarFromTimestamp(timestamp);;
		
		return
			calendarCur.get(Calendar.YEAR) == calendarTs.get(Calendar.YEAR) &&
			calendarCur.get(Calendar.MONTH) == calendarTs.get(Calendar.MONTH) &&
			calendarCur.get(Calendar.DAY_OF_MONTH) == calendarTs.get(Calendar.DAY_OF_MONTH);
	}
	
	public static boolean isTimeSlotHourRelevant(Timestamp timeslot) {
		Timestamp curTimestamp = new Timestamp(System.currentTimeMillis());		
		Calendar calendarCur = getCalendarFromTimestamp(curTimestamp);
		calendarCur.set(Calendar.HOUR_OF_DAY, 0);
		calendarCur.set(Calendar.MINUTE, 0);
		calendarCur.set(Calendar.SECOND, 0);
		calendarCur.set(Calendar.MILLISECOND, 0);
		
		if (timeslot.getTime() >= calendarCur.getTimeInMillis()) return true;
		return false;
	}
	
	public static String toCurrencyAmount(double amount, String currency) {
		return String.format("%.2f %s", Double.valueOf(amount), currency);
	}
	
	
	
	private static String formatTimeComponent(int value) {
		Integer component = Integer.valueOf(value);
		String strComponent = component.toString();
		if (strComponent.length() == 1) {
			strComponent = "0" + strComponent;
		}
		
		return strComponent;
	}
	
	private static Calendar getCalendarFromTimestamp(Timestamp timestamp) {
		Date date = new Date(timestamp.getTime());
		Calendar calendar = Calendar.getInstance((TimeZone.getTimeZone("UTC")));
		calendar.setTime(date);
		
		return calendar;
	}
}
