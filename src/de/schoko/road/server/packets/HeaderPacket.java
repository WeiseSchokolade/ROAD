package de.schoko.road.server.packets;

public class HeaderPacket extends Packet {
	public int version;
	public String name;
	public String carImageName;
	public String map;
	
	public HeaderPacket() {}
	
	public HeaderPacket(int version, String name, String carImageName, String map) {
		this.version = version;
		this.name = name;
		this.carImageName = carImageName;
		this.map = map;
	}
}
