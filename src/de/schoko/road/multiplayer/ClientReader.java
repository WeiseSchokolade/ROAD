package de.schoko.road.multiplayer;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;

import de.schoko.utility.Logging;

public class ClientReader extends Thread {
	private Vector<String> read;
	private Client client;
	private DataInputStream inputStream;
	
	public ClientReader(Client client) {
		this.client = client;
		this.inputStream = client.getDataInputStream();
		read = new Vector<>();
	}
	
	@Override
	public void run() {
		while (!client.isClosed()) {
			try {
				String read = inputStream.readUTF();
				if (read != null) {
					this.read.add(read);
				}
			} catch (IOException e) {
				Logging.logException(e);
			}
		}
	}
	
	public String getNextRead() {
		if (read.size() < 1) return null;
		String s = read.get(0);
		read.remove(0);
		return s;
	}
}
