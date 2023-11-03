package de.schoko.road.server;

import java.util.ArrayList;

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
		connections.add(connection);
		connection.setRoom(this);
	}
	
	public boolean hasSpace() {
		return connections.size() < 9;
	}
	
	public boolean isReady() {
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
		return "";
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
