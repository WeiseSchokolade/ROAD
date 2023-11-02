package de.schoko.road.server.shared.packets;

import de.schoko.road.server.shared.DisconnectionReason;

public class DisconnectionPacket extends Packet {
	public int id;
	public int code;
	public String message = "Unknown";
	public String extraData = "None";
	
	public DisconnectionPacket() {}
	
	public DisconnectionPacket(int id, int code, String message, String extraData) {
		this.id = id;
		this.code = code;
		this.message = message;
		this.extraData = extraData;
	}
	
	public DisconnectionPacket(DisconnectionReason reason, String extraData) {
		this(reason.getId(), reason.getCode(), reason.getMessage(), extraData);
	}

	public DisconnectionPacket(DisconnectionReason reason) {
		this(reason, "None");
	}
}
