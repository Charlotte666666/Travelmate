package com.jayrun.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FriendlyTimeUtil {
	public static String convertTimeToFormat(String unfriendlyString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = dateFormat.parse(unfriendlyString);
		} catch (ParseException e) {
			e.printStackTrace(); 
		}
		long unfriendlyTime = date.getTime()/1000;
		long curTime = System.currentTimeMillis() / (long) 1000;
		long time = curTime - unfriendlyTime;

		if (time < 60 && time >= 0) {
			return "�ո�";
		} else if (time >= 60 && time < 3600) {
			return time / 60 + "����ǰ";
		} else if (time >= 3600 && time < 3600 * 24) {
			return time / 3600 + "Сʱǰ";
		} else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
			return time / 3600 / 24 + "��ǰ";
		} else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
			return time / 3600 / 24 / 30 + "����ǰ";
		} else if (time >= 3600 * 24 * 30 * 12) {
			return time / 3600 / 24 / 30 / 12 + "��ǰ";
		} else {
			return "�ո�";
		}
	}
}
