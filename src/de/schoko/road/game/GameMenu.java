package de.schoko.road.game;

import de.schoko.rendering.Context;
import de.schoko.road.Map;
import de.schoko.road.Menu;

public abstract class GameMenu extends Menu {
	private CoreGame coreGame;
	
	public GameMenu(Map map) {
		coreGame = new CoreGame(map);
	}
	
	public GameMenu(CoreGame coreGame) {
		this.coreGame = coreGame;
	}
	
	@Override
	public void onLoad(Context context) {
		coreGame.onLoad(context);
		this.onLoad(context, coreGame);
	}
	public abstract void onLoad(Context context, CoreGame core);
	
	public CoreGame getCoreGame() {
		return coreGame;
	}
}
