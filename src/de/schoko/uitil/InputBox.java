package de.schoko.uitil;

import de.schoko.rendering.panels.Panel;

public abstract class InputBox extends Panel {
	private static InputBox selectedInputBox;
	
	protected abstract void apply();
	
	public boolean isSelected() {
		return selectedInputBox == this;
	}
	
	public static InputBox getSelectedInputBox() {
		return selectedInputBox;
	}
	
	public static void setSelectedInputBox(InputBox inputBox) {
		if (inputBox != selectedInputBox && selectedInputBox != null) selectedInputBox.apply();
		selectedInputBox = inputBox;
	}
}
