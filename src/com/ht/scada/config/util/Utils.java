package com.ht.scada.config.util;

import java.util.Calendar;
import java.util.Date;

public class Utils {
	// Date转Calendar
	public static Calendar date2CalendarUtil(Date date) {
		Calendar calendar = Calendar.getInstance(); // create Calendar Instance
		calendar.setTime(date); // 将刚才取得Date object 设给Calendar object
		return calendar;
	}
}
