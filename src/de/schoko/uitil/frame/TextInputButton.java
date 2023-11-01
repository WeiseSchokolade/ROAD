package de.schoko.uitil.frame;

import de.schoko.rendering.HUDGraph;
import de.schoko.uitil.TextInputBox;

public class TextInputButton extends Button {
	private TextInputBox textInputBox;
	private boolean constructed;
	private String defaultText;
	private int characterLimit;
	
	public TextInputButton(String defaultText, int characterLimit) {
		super(30);
		this.characterLimit = characterLimit;
		this.defaultText = defaultText;
	}
	
	@Override
	public void draw(HUDGraph hud) {
		if (!constructed) {
			constructed = true;
			// TODO: Fix this
			System.out.println("TextInputButton not working atm. DefaultText: " + defaultText + " CharacterLimit: " + characterLimit);
			//this.textInputBox = new TextInputBox(getContext(), getX(), getY(), defaultText, characterLimit);
		}
		
		textInputBox.setX(this.getX());
		textInputBox.setY(this.getY());
		
		//hud.draw(textInputBox);
	}

	public String getText() {
		return textInputBox.getString();
	}
	
	public boolean isSelected() {
		return textInputBox.isSelected();
	}
}
