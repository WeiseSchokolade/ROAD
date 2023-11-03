package de.schoko.road.server.shared.packets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Packet {
	private String type;
	private long sendTime;
	
	public Packet() {
		type = getClass().getSimpleName();
		sendTime = System.currentTimeMillis();
	}
	
	public String getType() {
		return type;
	}
	
	public long getSendTime() {
		return sendTime;
	}
	
	public static Gson getGson() {
		return new GsonBuilder()
					.create();
	}
	
	public static String toJson(Packet packet) {
		return getGson().toJson(packet);
	}
}
