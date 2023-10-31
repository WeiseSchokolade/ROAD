package de.schoko.road.server;

import de.schoko.road.server.packets.Packet;
import de.schoko.serverbase.core.Connection;

public class LobbyConnection {
	private Connection connection;
	
	private int version;
	private String name;
	private String map;
	private boolean ready;
	
	public LobbyConnection(Connection connection) {
		this.connection = connection;
	}
	
	public void send(String s) {
		connection.send(s);
	}
	
	public void send(Packet packet) {
		connection.send(Packet.toJson(packet));
	}
	
	public String read() {
		return connection.read();
	}
	
	public void close() {
		connection.close();
	}
	
	public boolean isClosed() {
		return connection.isClosed();
	}
	
	public int getVersion() {
		return version;
	}
	
	public String getName() {
		return name;
	}
	
	public String getMap() {
		return map;
	}
	
	public boolean isReady() {
		return ready;
	}
	
	public void setVersion(int version) {
		this.version = version;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setMap(String map) {
		this.map = map;
	}
	
	public void setReady(boolean ready) {
		this.ready = ready;
	}
	
	public Connection getConnection() {
		return connection;
	}
}
