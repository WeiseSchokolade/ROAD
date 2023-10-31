package de.schoko.road.server;

import java.util.ArrayList;

public class MapStatus {
	private String name;
	private ArrayList<LobbyConnection> players;
	
	public MapStatus(String name) {
		this.name = name;
		players = new ArrayList<>();
	}
	
	public void update() {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).isClosed()) {
				players.remove(i);
				i--;
			}
		}
	}
	
	public boolean isReady() {
		if (players.size() <= 1) return false;
		if (players.size() > 6) return true;
		for (int i = 0; i < players.size(); i++) {
			if (!players.get(i).isReady()) {
				return false;
			}
		}
		return true;
	}
	
	public int getReadyAmount() {
		int amt = 0;
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).isReady()) amt++;
		}
		return amt;
	}
	
	public void addPlayer(LobbyConnection player) {
		if (players.contains(player)) return;
		players.add(player);
	}
	
	public String[] getPlayerNames() {
		String[] names = new String[6];
		for (int i = 0; i < names.length; i++) {
			if (i >= players.size()) {
				names[i] = "";
				continue;
			}
			names[i] = players.get(i).getName();
		}
		return names;
	}
	
	public ArrayList<LobbyConnection> getReadyPlayers() {
		ArrayList<LobbyConnection> readyPlayers = new ArrayList<>();
		int playerAmount = 0;
		while (playerAmount < 7 && players.size() > 0) {
			playerAmount++;
			readyPlayers.add(players.remove(0));
		}
		return readyPlayers;
	}
	
	public ArrayList<LobbyConnection> getPlayers() {
		return players;
	}
	
	public String getName() {
		return name;
	}
}
