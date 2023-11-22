package de.schoko.road.multiplayer;

import de.schoko.road.server.shared.packets.Packet;

public interface PacketHandler {
	public void handle(Packet packet);
}
