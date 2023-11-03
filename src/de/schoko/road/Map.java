package de.schoko.road;

import de.schoko.road.geometry.CatmullRomSpline;
import de.schoko.road.geometry.Vector2D;

public class Map {
	public static final int VERSION = 1;
	public static final String FILE_EXTENSION = ".map";
	private CatmullRomSpline catmullRomSpline;
	private Vector2D entry;
	private Vector2D entryDirection;
	private String fileName;
	private String name;
	private String description;
	
	public Map() {
		catmullRomSpline = new CatmullRomSpline();
		entry = new Vector2D();
		entryDirection = new Vector2D(0, 1);
		fileName = "map";
		name = "Map";
		description = "A cool map!";
	}
	
	public CatmullRomSpline getCatmullRomSpline() {
		return catmullRomSpline;
	}
	
	public Vector2D getEntry() {
		return entry;
	}
	
	public Vector2D getEntryDirection() {
		return entryDirection;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
