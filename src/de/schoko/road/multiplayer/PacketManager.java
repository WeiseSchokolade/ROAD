package de.schoko.road.multiplayer;

import java.util.HashMap;

import com.google.gson.Gson;

import de.schoko.road.server.shared.packets.Packet;

public class PacketManager {
	private HashMap<String, PacketHandler> handlers;
	private HashMap<String, Class<? extends Packet>> classes;
	private Client client;
	
	private PacketManager() {
		handlers = new HashMap<>();
		classes = new HashMap<>();
	}
	
	public PacketManager(Client client) {
		this();
		this.client = client;
	}
	
	public void update() {
		Gson gson = Packet.getGson();

		String read;
		if ((read = client.read()) != null) {
			if (read != null) {
				Packet packet = gson.fromJson(read, Packet.class);
				handle(gson, packet.getType(), read);
			}
		}
	}

	public void handle(Gson gson, String packageType, String data) {
		if (handlers.containsKey(packageType)) {
			Packet packet = gson.fromJson(data, classes.get(packageType));
			handlers.get(packageType).handle(packet);
		} else {
			throw new IllegalArgumentException("Unknown type: " + packageType);
		}
	}
	
	public void registerHandler(Class<? extends Packet> clazz, PacketHandler handler) {
		handlers.put(clazz.getSimpleName(), handler);
		classes.put(clazz.getSimpleName(), clazz);
	}
}
