package de.schoko.uitil;

import de.schoko.rendering.hud.DrawCall;

public abstract class InputBox extends DrawCall {
	protected static InputBox selectedInputBox;
	
	public static InputBox getSelectedInputBox() {
		return selectedInputBox;
	}
}
