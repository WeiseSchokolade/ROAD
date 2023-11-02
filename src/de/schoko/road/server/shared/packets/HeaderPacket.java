package de.schoko.road.server.shared.packets;

public class HeaderPacket extends Packet {
	public int version;
	public String edition;
	public String name;
	public String carImageName;
	public String map;
	
	public HeaderPacket() {}
	
	public HeaderPacket(int version, String edition, String name, String carImageName, String map) {
		this.version = version;
		this.edition = edition;
		this.name = name;
		this.carImageName = carImageName;
		this.map = map;
	}
}
