package de.schoko.road.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.schoko.road.Constants;
import de.schoko.road.ResourceCopy;
import de.schoko.road.server.shared.SharedConstants;
import de.schoko.saving.config.Config;
import de.schoko.saving.config.ConfigSettings;
import de.schoko.saving.config.ResourceLocation;
import de.schoko.serverbase.Application;
import de.schoko.serverbase.ApplicationProvider;
import de.schoko.serverbase.Server;
import de.schoko.serverbase.core.ApplicationThread;
import de.schoko.serverbase.core.Connection;
import de.schoko.serverbase.ext.ConsoleCommandManager;

public class ServerStarter {
	public static void main(String[] args) {
		int port = 5555;
		int tps = 200;
		
		ResourceCopy resourceCopy = new ResourceCopy();
		
		String parentDir = "maps/";
		File dirFile = new File(parentDir);
		if (dirFile.exists()) {
			System.out.println("Not loading maps");
		} else {
			System.out.println("Loading maps from archive");
			dirFile.mkdirs();
			try {
				resourceCopy.copyResourcesToDir(dirFile, false,
						Constants.RESOURCE_PATH + "levels/track_1.map",
						Constants.RESOURCE_PATH + "levels/track_2.map",
						Constants.RESOURCE_PATH + "levels/track_3.map");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String[] maps = dirFile.list();
		for (int i = 0; i < maps.length; i++) {
			maps[i] = maps[i].substring(0, maps[i].length() - ".map".length());
		}
		System.out.print("Maps: ");
		for (int i = 0; i < maps.length; i++) {
			System.out.print(maps[i]);
			if (i < maps.length - 1) {
				 System.out.print(", ");
			} else {
				System.out.println();
			}
		}
		
		ConfigSettings.getGlobal().setBaseResourcePath(Constants.RESOURCE_PATH);
		settings = Config.getConfig("config/server_settings.txt", ResourceLocation.FILE, "server_settings.txt", ResourceLocation.JAR);
		if (settings != null) {
			String enteredPort = settings.get("port");
			if (enteredPort != null) {
				port = Integer.valueOf(enteredPort);
			} else {
				settings.set("port", "" + port);
			}
			String enteredTPS = settings.get("tps");
			if (enteredTPS != null) {
				tps = Integer.valueOf(enteredTPS);
			} else {
				settings.set("tps", "" + tps);
			}
		} else {
			System.err.println("Couldn't load config!");
		}
		try {
			settings.write("config/server_settings.txt", ResourceLocation.FILE);
		} catch (IOException e) {
			System.err.println("Couldn't save server settings!");
			e.printStackTrace();
		}
		
		if (args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				System.out.println("Couldn't parse " + args[0] + "as the port, using default port " + port);
			}
		}
		System.out.println("Starting Server for R.O.A.D. " + SharedConstants.EDITION + " protocol version " + SharedConstants.PROTOCOL_VERSION + " on port " + port);
		
		Server server = new Server(port, new ApplicationProvider() {
			@Override
			public void handleConnection(Server server, Connection connection) {
				System.out.println("New Connection: " + connection.getUID());
				thread.getApplication().addConnection(connection);
			}
		}, tps);
		ConsoleCommandManager consoleManager = new ConsoleCommandManager();
		consoleManager.registerCommand("stop", (arguments) -> {
			System.exit(0);
		});
		consoleManager.registerCommand("echo", (arguments) -> {
			System.out.println(arguments);
		});
		String helpMessage = """
				 ----- Help -----
				Commands:
				 - help 						Sends this message
				 - stop 						Stops the server
				 - info							Shows information about the server
				 - echo [args...] 				Echos provided arguments
				 - list {threads|applications}	Lists data of the server
				""";
		consoleManager.registerCommand("help", (arguments) -> {
			System.out.println(helpMessage);
			
		});
		consoleManager.registerCommand("list", (argument) -> {
			String[] arguments = argument.split(" ");
			if (arguments.length == 0) {
				System.out.println("Arguments: threads, applications");
			} else if (arguments.length == 1) {
				if (arguments[0].equalsIgnoreCase("threads")) {
					ArrayList<ApplicationThread> threads = server.getApplicationThreads();
					System.out.println("Threads: " + threads.toString());
				} else if (arguments[0].equalsIgnoreCase("applications")) {
					ArrayList<Application> applications = server.getApplications();
					System.out.println("Applications: " + applications.toString());
				} else {
					System.err.println("Unknown argument: " + arguments[0]);
				}
			} else {
				System.err.println("Too many arguments for 'list': " + arguments.length);
			}
		});
		final int finalPort = port;
		final int finalTPS = tps;
		consoleManager.registerCommand("info", arguments -> {
			System.out.println(" ----- Server Information -----");
			System.out.println("Port: " + finalPort);
			System.out.println("Goal TPS: " + finalTPS);
			System.out.println("Application Threads: " + server.getApplicationThreads());
			System.out.println("Connections: " + server.getConnections().size());
			System.out.println(" ----- Server Information -----");
		});
		server.setConsoleHandler(consoleManager);
		thread = server.startApplication(new ServerLobby(server, maps));
		server.run();
	}
	
	private static Config settings;
	private static ApplicationThread thread;
	
	public static Config getSettings() {
		return settings;
	}
}
