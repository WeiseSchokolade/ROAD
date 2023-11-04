package de.schoko.road.server;

import java.util.ArrayList;
import java.util.HashMap;

import de.schoko.road.server.shared.packets.LobbyStatusPacket;

public class Room {
	private ArrayList<LobbyConnection> connections;
	private String[] mapNames;
	private boolean dirty;
	
	public Room(String[] mapNames) {
		this.mapNames = mapNames;
		connections = new ArrayList<>();
	}
	
	public void update() {
		clean();
		if (dirty) {
			dirty = false;
			updateStatus();
		}
	}
	
	public void clean() {
		for (int i = 0; i < connections.size(); i++) {
			if (connections.get(i).isClosed()) {
				connections.remove(i);
				dirty = true;
			}
		}
	}
	
	public void updateStatus() {
		for (int i = 0; i < connections.size(); i++) {
			LobbyConnection connection = connections.get(i);
			connection.send(new LobbyStatusPacket(getReadyAmount(), getPlayerNames(), mapNames, connections.size()));
		}
	}
	
	public void addConnection(LobbyConnection connection) {
		dirty = true;
		connections.add(connection);
		connection.setRoom(this);
	}
	
	public boolean hasSpace() {
		return connections.size() < 9;
	}
	
	public boolean isReady() {
		if (connections.size() < 2) return false;
		for (int i = 0; i < connections.size(); i++) {
			if (!connections.get(i).isReady()) return false;
		}
		return true;
	}
	
	public int getReadyAmount() {
		int readyAmount = 0;
		for (int i = 0; i < connections.size(); i++) {
			if (connections.get(i).isReady()) {
				readyAmount++;
			}
		}
		return readyAmount;
	}
	
	public String getMap() {
		HashMap<String, Integer> maps = new HashMap<>();
		for (String map : mapNames) {
			maps.put(map, 0);
		}
		for (int i = 0; i < connections.size(); i++) {
			LobbyConnection connection = connections.get(i);
			String votedMap = connection.getVotedMap();
			if (votedMap != null) {
				maps.put(votedMap, maps.get(votedMap) + 1);
			}
		}
		String[] highestMap = new String[] {mapNames[(int) Math.floor(Math.random() * mapNames.length)]};
		int[] highestVotes = new int[] {0};
		maps.forEach((map, votes) -> {
			if (votes == highestVotes[0]) {
				if (Math.random() < 0.5) {
					highestMap[0] = map;
				}
			}
			if (votes > highestVotes[0]) {
				highestMap[0] = map;
				highestVotes[0] = votes;
				return;
			}
		});
		return highestMap[0];
	}

	public String[] getPlayerNames() {
		String[] names = new String[connections.size()];
		for (int i = 0; i < names.length; i++) {
			names[i] = connections.get(i).getName();
		}
		return names;
	}
	
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
	public ArrayList<LobbyConnection> getConnections() {
		return connections;
	}
}
