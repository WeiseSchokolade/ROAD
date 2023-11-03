package de.schoko.road.server.shared.packets;

public class GameStartPacket extends Packet {
	public String[] players;
	public String mapData;
	public int playerID;
	public long startTime;
	
	public GameStartPacket() {}
	
	public GameStartPacket(String[] players, String mapData, int playerID, long startTime) {
		this.players = players;
		this.mapData = mapData;
		this.playerID = playerID;
		this.startTime = startTime;
	}
}
