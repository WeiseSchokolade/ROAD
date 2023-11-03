package de.schoko.road.server.shared.packets;

public class HeaderPacket extends Packet {
	public int version;
	public String edition;
	public String name;
	public String carImageName;
	
	public HeaderPacket() {}
	
	public HeaderPacket(int version, String edition, String name, String carImageName) {
		this.version = version;
		this.edition = edition;
		this.name = name;
		this.carImageName = carImageName;
	}
}
