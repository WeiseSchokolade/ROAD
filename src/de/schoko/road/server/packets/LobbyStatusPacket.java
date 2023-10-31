package de.schoko.road.server.packets;

public class LobbyStatusPacket extends Packet {
	public int readyAmount;
	public String[] players;
	public int totalPlayers;
	
	public LobbyStatusPacket() {}
	
	public LobbyStatusPacket(int readyAmount, String[] players, int totalPlayers) {
		this.readyAmount = readyAmount;
		this.players = players;
		this.totalPlayers = totalPlayers;
	}
}
