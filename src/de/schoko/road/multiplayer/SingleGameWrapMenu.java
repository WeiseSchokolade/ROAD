package de.schoko.road.multiplayer;

import de.schoko.rendering.Context;
import de.schoko.rendering.Graph;
import de.schoko.road.Menu;
import de.schoko.road.game.Car;
import de.schoko.road.game.PlayerCar;
import de.schoko.road.game.RemoteCar;
import de.schoko.road.game.SingleGame;
import de.schoko.road.server.shared.packets.CarInfoPacket;
import de.schoko.road.server.shared.packets.GamePlayersUpdatePacket;
import de.schoko.road.server.shared.packets.Packet;
import de.schoko.serverbase.PacketReducer;
import de.schoko.utility.TimeLogger;

public class SingleGameWrapMenu extends Menu {
	private SingleGame singleGame;
	private Client client;
	private PacketReducer packetReducer;
	
	public SingleGameWrapMenu(SingleGame singleGame, Client client) {
		this.singleGame = singleGame;
		this.client = client;
		packetReducer = new PacketReducer(client, 60);
	}
	
	@Override
	public void onLoad(Context context) {
		singleGame.setContext(context);
		singleGame.onLoad(context);
	}
	
	@Override
	public void onChange() {
		client.close();
	}
	
	@Override
	public void update(double deltaTime) {
		singleGame.update(deltaTime);

		TimeLogger.start("wrapNetwork");
		String read;
		
		while ((read = client.read()) != null) {
			Packet packet = Packet.getGson().fromJson(read, Packet.class);
			switch (packet.getType()) {
			case "GamePlayersUpdatePacket":
				GamePlayersUpdatePacket p0 = Packet.getGson().fromJson(read, GamePlayersUpdatePacket.class);
				for (int i = 0; i < singleGame.getCars().size(); i++) {
					Car car = singleGame.getCars().get(i);
					if (car instanceof PlayerCar) continue;
					if (car instanceof RemoteCar remoteCar) {
						CarInfoPacket p = p0.players[i];
						p.apply(remoteCar);
						long sendTime = p.lastUpdate;
						long currentTime = System.currentTimeMillis();
						double passedTime = (currentTime - sendTime) / 1000.0;
						if (passedTime < 100000) {
							remoteCar.update(passedTime);
						}
					}
				}
				break;
			case "CarInfoPacket":
				CarInfoPacket p1 = Packet.getGson().fromJson(read, CarInfoPacket.class);
				p1.apply(singleGame.getPlayerCar());
				break;
			default:
				System.out.println("Unknown packet: " + read);
				break;
			}
		}
		
		packetReducer.send(Packet.toJson(new CarInfoPacket(singleGame.getPlayerCar())), deltaTime * 1000);
		TimeLogger.end("wrapNetwork");
	}

	@Override
	public void render(Graph g) {
		singleGame.render(g);
	}
	
}
