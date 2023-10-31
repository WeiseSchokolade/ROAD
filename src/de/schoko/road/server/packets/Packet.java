package de.schoko.road.server.packets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Packet {
	private String type;
	
	public Packet() {
		type = getClass().getSimpleName();
	}
	
	public String getType() {
		return type;
	}
	
	public static Gson getGson() {
		return new GsonBuilder()
					.create();
	}
	
	public static String toJson(Packet packet) {
		return getGson().toJson(packet);
	}
}
