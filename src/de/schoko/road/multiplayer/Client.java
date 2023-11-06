package de.schoko.road.multiplayer;

import java.io.IOException;
import java.net.UnknownHostException;

import de.schoko.road.server.shared.packets.Packet;

public class Client extends de.schoko.serverbase.core.Client {
	private ClientReader reader;
	
	public Client(String ip, int port) throws UnknownHostException, IOException {
		super(ip, port);
		reader = new ClientReader(this);
		reader.start();
	}
	
	public void send(Packet packet) {
		super.send(Packet.getGson().toJson(packet));
	}
	
	@Override
	public String read() {
		return reader.getNextRead();
	}
}
