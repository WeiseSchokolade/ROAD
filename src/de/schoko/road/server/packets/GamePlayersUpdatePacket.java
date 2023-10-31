package de.schoko.road.server.packets;

public class GamePlayersUpdatePacket extends Packet {
	public CarInfoPacket[] players;
	
	public GamePlayersUpdatePacket() {};
	
	public GamePlayersUpdatePacket(CarInfoPacket[] players) {
		this.players = players;
	}
}
