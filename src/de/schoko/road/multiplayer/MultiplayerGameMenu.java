package de.schoko.road.multiplayer;

import java.util.List;

import de.schoko.rendering.Camera;
import de.schoko.rendering.Context;
import de.schoko.rendering.Graph;
import de.schoko.rendering.HUDGraph;
import de.schoko.rendering.Image;
import de.schoko.rendering.ImageLocation;
import de.schoko.road.Constants;
import de.schoko.road.GameCompleteMenu;
import de.schoko.road.RoadProject;
import de.schoko.road.game.Car;
import de.schoko.road.game.CoreGame;
import de.schoko.road.game.GameMenu;
import de.schoko.road.game.PlayerCar;
import de.schoko.road.game.RemoteCar;
import de.schoko.road.geometry.Vector2D;
import de.schoko.road.server.shared.packets.CarInfoPacket;
import de.schoko.road.server.shared.packets.GamePlayersUpdatePacket;
import de.schoko.road.server.shared.packets.GameStartPacket;
import de.schoko.road.server.shared.packets.Packet;
import de.schoko.serverbase.PacketReducer;
import de.schoko.utility.TimeLogger;

public class MultiplayerGameMenu extends GameMenu {
	private Client client;
	private PacketManager manager;
	private PacketReducer packetReducer;

	private boolean carCompleted = false;
	private double camX, camY;
	private int unfinishedCarAmount;
	
	private Image[] lights;
	private GameStartPacket packet;
	private boolean started;

	private double lightY;
	private boolean lightHidden;
	private double lightImageHeight;
	
	public MultiplayerGameMenu(CoreGame coreGame, Client client, GameStartPacket packet) {
		super(coreGame);
		this.client = client;
		this.packet = packet;
		packetReducer = new PacketReducer(client, 25);
		manager = new PacketManager(client);
	}

	@Override
	public void onLoad(Context context, CoreGame core) {
		manager.registerHandler(GamePlayersUpdatePacket.class, (data) -> {
			GamePlayersUpdatePacket packet = (GamePlayersUpdatePacket) data;
			for (int i = 0; i < core.getCars().size(); i++) {
				Car car = core.getCars().get(i);
				if (car instanceof PlayerCar) continue;
				if (car instanceof RemoteCar remoteCar) {
					CarInfoPacket p = packet.players[i];
					Vector2D pos = remoteCar.getPos();
					p.apply(remoteCar);
					if (p.lastUpdate == 0) remoteCar.setPos(pos);
					long sendTime = p.lastUpdate;
					long currentTime = System.currentTimeMillis();
					double passedTime = (currentTime - sendTime) / 1000.0;
					if (passedTime < 100000) {
						remoteCar.update(passedTime);
					}
				}
			}
		});
		manager.registerHandler(CarInfoPacket.class, (data) -> {
			CarInfoPacket packet = (CarInfoPacket) data;
			packet.apply(core.getPlayerCar());
		});
		
		Camera camera = getContext().getCamera();
		camera.setZoomable(false);
		camera.setMovable(false);

		lights = new Image[5];
		lights[0] = context.getImagePool().getImage("light_0", Constants.RESOURCE_PATH + "light_0.png", ImageLocation.JAR);
		lights[1] = context.getImagePool().getImage("light_1", Constants.RESOURCE_PATH + "light_1.png", ImageLocation.JAR);
		lights[2] = context.getImagePool().getImage("light_2", Constants.RESOURCE_PATH + "light_2.png", ImageLocation.JAR);
		lights[3] = context.getImagePool().getImage("light_3", Constants.RESOURCE_PATH + "light_3.png", ImageLocation.JAR);
		lights[4] = context.getImagePool().getImage("light_4", Constants.RESOURCE_PATH + "light_4.png", ImageLocation.JAR);
		lightImageHeight = lights[0].getHeight() / 0.3;
		
		camera.setX(core.getPlayerCar().getPos().getX());
		camera.setY(core.getPlayerCar().getPos().getY());
	}
	
	@Override
	public void onChange() {
		client.close();
	}

	@Override
	public void update(double deltaTime) {
		Camera camera = getContext().getCamera();
		
		if (getCoreGame().getPlayerCar().hasCompleted()) {
			if (carCompleted) {
				camera.setX(camX);
				camera.setY(camY);
				
				List<Car> cars = getCoreGame().getCars();
				unfinishedCarAmount = cars.size();
				for (int i = 0; i < cars.size(); i++) {
					if (cars.get(i).hasCompleted()) {
						unfinishedCarAmount--;
					}
				}
				if (unfinishedCarAmount < 1) {
					RoadProject.get().setMenu(new GameCompleteMenu(getCoreGame(), packet.startTime));
					return;
				}
			} else {
				carCompleted = true;
				camX = camera.getX();
				camY = camera.getY();
			}
		}
		
		if (started) {
			getCoreGame().update(deltaTime);
			if (!lightHidden) {
				long currentTimeMillis = System.currentTimeMillis();
				double timeUntilStart = (packet.startTime - currentTimeMillis) / 1000.0;
				lightY = timeUntilStart * 150;
				if (lightY < -lightImageHeight) {
					lightHidden = true;
				}
			}
		} else {
			long currentTimeMillis = System.currentTimeMillis();
			double timeUntilStart = (packet.startTime - currentTimeMillis) / 1000.0;
			if (currentTimeMillis > packet.startTime) {
				started = true;
				update(-timeUntilStart);
				return;
			}
			double zoomTime = (packet.startTime - currentTimeMillis - 3500) / 1000.0;
			camera.setZoom(Math.min(150 - zoomTime * 25, 150));
		}
		TimeLogger.start("wrapNetwork");
		manager.update();
		packetReducer.send(Packet.toJson(new CarInfoPacket(getCoreGame().getPlayerCar())), deltaTime * 1000);
		TimeLogger.end("wrapNetwork");
	}

	@Override
	public void render(Graph g) {
		Camera camera = getContext().getCamera();
		camera.setX(getCoreGame().getPlayerCar().getPos().getX());
		camera.setY(getCoreGame().getPlayerCar().getPos().getY());
		HUDGraph hud = g.getHUD();
		getCoreGame().draw(g);
		if (!lightHidden) {
			long currentTimeMillis = System.currentTimeMillis();
			double timeUntilStart = (packet.startTime - currentTimeMillis) / 1000.0;
			hud.drawImage((hud.getWidth() - (lights[0].getWidth() / 0.3)) / 2, lightY, lights[(int) Math.max(Math.min(4 - timeUntilStart, 4), 0)], 0.3);
			g.addDebugString("TimeUntilStart: " + timeUntilStart);
		}
	}

}
