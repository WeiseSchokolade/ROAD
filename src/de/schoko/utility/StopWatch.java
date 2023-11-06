package de.schoko.utility;

public class StopWatch {
	private static StopWatch instance;
	
	private boolean started;
	private long startTime;
	private long stopTime;
	
	public StopWatch() {
		
	}
	
	public static void startNew() {
		instance = new StopWatch();
		instance.start();
	}
	
	public static void stopNew() {
		System.out.println("Stopped Time: " + instance.stop());
	}
	
	public static StopWatch getStarted() {
		StopWatch watch = new StopWatch();
		watch.start();
		return watch;
	}
	
	public void start() {
		started = true;
		this.startTime = System.currentTimeMillis();
	}
	
	public long currentDeltaMS() {
		if (!started) return -1;
		return System.currentTimeMillis() - startTime;
	}
	
	public long stop() {
		started = false;
		stopTime = System.currentTimeMillis();
		return stopTime - startTime;
	}
}
