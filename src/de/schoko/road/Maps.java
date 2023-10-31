package de.schoko.road;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Maps {
	public static final String[] maps = {
			"track_1",
			"track_2",
			"track_3"
	};
	
	public static void saveMap(Map map) {
		Gson gson = new GsonBuilder()
					.create();
		String json = gson.toJson(map);
		String parentDir = System.getenv("APPDATA") + File.separator + "road";
		try {
			String fileName = parentDir + File.separator + map.getFileName() + Map.FILE_EXTENSION;
			System.out.println("Saving Map: " + fileName);
			File file = new File(fileName);
			file.getParentFile().mkdirs();
			file.createNewFile();
			Files.writeString(Path.of(fileName), json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Map loadMap(String fileName) {
		Gson gson = new GsonBuilder()
				.create();
		String parentDir = System.getenv("APPDATA") + File.separator + "road";
		String path = parentDir + File.separator + fileName + Map.FILE_EXTENSION;
		System.out.println("Loading Map: " + path);
		try {
			String json = Files.readString(Path.of(path));
			Map map = gson.fromJson(json, Map.class);
			map.setFileName(fileName);
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
