package de.schoko.road.server.shared.packets;

public class VotePacket extends Packet {
	public String map;
	
	public VotePacket() {
	}
	
	public VotePacket(String map) {
		this.map = map;
	}
}
