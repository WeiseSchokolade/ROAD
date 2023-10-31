package de.schoko.road.multiplayer;

import java.io.IOException;
import java.net.UnknownHostException;

import de.schoko.road.server.packets.Packet;

public class Client extends de.schoko.serverbase.core.Client {
	public Client(String ip, int port) throws UnknownHostException, IOException {
		super(ip, port);
	}
	
	public void send(Packet packet) {
		super.send(Packet.getGson().toJson(packet));
	}
}
