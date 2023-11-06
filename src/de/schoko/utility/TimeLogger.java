package de.schoko.utility;

import java.util.HashMap;
import java.util.Set;

public class TimeLogger {
	private static HashMap<String, Long> startTimes = new HashMap<>();
	private static HashMap<String, Long> times = new HashMap<>();
	
	public static void start(String key) {
		startTimes.put(key, System.currentTimeMillis());
	}
	
	public static void end(String key) {
		times.put(key, System.currentTimeMillis() - startTimes.get(key));
	}
	
	public static long getTime(String key) {
		return times.get(key);
	}
	
	public static Set<String> getKeys() {
		return times.keySet();
	}
}
