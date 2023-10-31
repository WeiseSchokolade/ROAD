package de.schoko.road;

import java.util.Arrays;

import de.schoko.road.server.ServerStarter;

public class ProjectStarter {
	public static void main(String[] args) {
		if (args.length > 0) {
			if (args[0].equals("server")) {
				String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
				ServerStarter.main(newArgs);
				return;
			}
			if (args[0].equals("dev")) {
				Constants.DEV_ACCESS = true;
				Constants.RESOURCE_PATH = "de/schoko/road/";
			}
		}
		RoadProject.main(args);
	}
}
