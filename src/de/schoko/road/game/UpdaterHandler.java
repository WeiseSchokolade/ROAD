package de.schoko.road.game;

import java.util.ArrayList;
import java.util.List;

public class UpdaterHandler {
	private List<UpdaterHolder> holders;
	
	public UpdaterHandler() {
		holders = new ArrayList<>();
	}
	
	public void update(double deltaTime) {
		long currentTime = System.currentTimeMillis();
		for (int i = 0; i < holders.size(); i++) {
			UpdaterHolder holder = holders.get(i);
			if (holder.isRemoved()) {
				holders.remove(i);
				i--;
				continue;
			}
			holder.call(currentTime, deltaTime);
		}
	}
	
	public void addUpdater(Updater updater, long callTime) {
		holders.add(new UpdaterHolder(updater, callTime));
	}
	
	public class UpdaterHolder {
		private Updater updater;
		private final long callTime;
		private long lastCallTime;
		private long nextCallTime;
		private boolean removed;
		
		public UpdaterHolder(Updater updater, long delay) {
			this.updater = updater;
			this.callTime = delay;
			this.nextCallTime = callTime;
		}
		
		public void call(long currentTime, double deltaTime) {
			if (removed) return;
			if (currentTime > nextCallTime) {
				lastCallTime = nextCallTime;
				nextCallTime = lastCallTime + callTime;
				updater.update(deltaTime);
			}
		}
		
		public void remove() {
			this.removed = true;
		}
		
		public boolean isRemoved() {
			return removed;
		}
	}
}
