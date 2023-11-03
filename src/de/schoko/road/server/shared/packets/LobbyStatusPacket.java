package de.schoko.road.server.shared.packets;

public class LobbyStatusPacket extends Packet {
	public int readyAmount;
	public String[] players;
	public String[] maps;
	public int totalPlayers;
	
	public LobbyStatusPacket() {}
	
	public LobbyStatusPacket(int readyAmount, String[] players, String[] maps, int totalPlayers) {
		this.readyAmount = readyAmount;
		this.players = players;
		this.maps = maps;
		this.totalPlayers = totalPlayers;
	}
}
