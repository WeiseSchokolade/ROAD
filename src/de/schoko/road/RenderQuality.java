package de.schoko.road;

public enum RenderQuality {
	BASIC("Basic", "The ugliest option, but high fps. Has visual bugs.", 0.5, 0, false),
	FAST("Fast", "Fast. Better than basic. Has visual bugs", 0.25, 150, false),
	NORMAL("Normal", "The expected quality. Might have bugs", 0.05, 300, false),
	HIGH("High", "Normal without any bugs.", 0.05, 300, true),
	ULTRA("Ultra", "The best option. Not really better than high but just for flexing.", 0.01, Integer.MAX_VALUE, true)
	;
	
	private String name;
	private String description;
	private double roadDetail;
	private int roadOutOfBoundsHide;
	private boolean smoothEdges;

	private RenderQuality(String name, String description, double roadDetail, int roadOutOfBoundsHide, boolean smoothEdges) {
		this.name = name;
		this.description = description;
		this.roadDetail = roadDetail;
		this.roadOutOfBoundsHide = roadOutOfBoundsHide;
		this.smoothEdges = smoothEdges;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public double getRoadDetail() {
		return roadDetail;
	}
	
	public int getRoadOutOfBoundsHide() {
		return roadOutOfBoundsHide;
	}
	
	public boolean hasSmoothEdges() {
		return smoothEdges;
	}
}
