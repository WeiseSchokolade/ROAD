package de.schoko.road.server.packets;

public class GameStartPacket extends Packet {
	public String[] players;
	public String mapName;
	public int playerID;
	public long startTime;
	
	public GameStartPacket() {}
	
	public GameStartPacket(String[] players, String mapName, int playerID, long startTime) {
		this.players = players;
		this.mapName = mapName;
		this.playerID = playerID;
		this.startTime = startTime;
	}
}
