package de.schoko.road.server.shared.packets;

public class LobbyReadyPacket extends Packet {
	public boolean ready;
	
	public LobbyReadyPacket() {}
	
	public LobbyReadyPacket(boolean ready) {
		this.ready = ready;
	}
}
