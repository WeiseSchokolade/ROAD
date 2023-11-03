package de.schoko.road.server.shared;

import java.awt.Color;

import de.schoko.rendering.Graph;
import de.schoko.road.game.SingleGame;

public final class SharedConstants {
	/**
	 * Protocol version for server connections. This number is increased every time
	 * <ul>
	 *   <li>
	 *   A backwards-compatibility breaking change is made.
	 *   </li>
	 *   <li>
	 *   A change to the contents of the package de.schoko.road.server.shared is made.
	 *   </li>
	 * </ul>
	 */
	public static final int PROTOCOL_VERSION = 5;
	
	/**
	 * Edition of the game. This should only be changed by modders and developers of forks.
	 */
	public static final String EDITION = "Vanilla";
	
	/**
	 * Order of the colors of cars when added to {@link SingleGame}.
	 */
	public static final Color[] COLOR_ORDER = {
			Graph.getColor(0, 0, 255),
			Graph.getColor(255, 0, 0),
			Graph.getColor(0, 255, 0),
			Graph.getColor(255, 255, 0),
			Graph.getColor(0, 255, 255),
			Graph.getColor(255, 0, 255),
			Graph.getColor(255, 127, 0),
			Graph.getColor(0, 255, 127),
			Graph.getColor(127, 0, 255)
	};
	
	/**
	 * Names of maps that are available to play.
	 */
	public static final String[] MAP_NAMES = {
			"track_1",
			"track_2",
			"track_3"	
	};
}
