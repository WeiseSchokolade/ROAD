package de.schoko.uitil.frame;

import java.awt.Color;

import de.schoko.rendering.Graph;
import de.schoko.rendering.HUDGraph;
import de.schoko.uitil.TextInputBox;

public class LabelledTextInputButton extends Button {
	private TextInputBox textInputBox;
	private boolean constructed;
	private String label;
	private String defaultText;
	private int labelWidth;
	
	public LabelledTextInputButton(String label, String defaultText) {
		super(30);
		this.label = label;
		this.defaultText = defaultText;
	}
	
	@Override
	public void draw(HUDGraph hud) {
		construct();
		textInputBox.setX(this.getX() + labelWidth + 5);
		textInputBox.setY(this.getY());
		
		hud.drawText(label, getX(), getY() + textInputBox.getFont().getSize(), Color.WHITE, textInputBox.getFont());
		// TODO: Make this line work
		//hud.draw(textInputBox);
	}
	
	public void construct() {
		if (constructed) return;
		constructed = true;
		// TODO: Make this line work
		System.out.println("LabelledTextInputButton not working atm. DefaultText: " + defaultText);
		//this.textInputBox = new TextInputBox(getContext(), getX(), getY(), defaultText, 30);
		this.labelWidth = Graph.getStringWidth(label, this.textInputBox.getFont());
		this.textInputBox.setX(getX() + labelWidth + 5);
	}

	public String getText() {
		return textInputBox.getString();
	}
	
	public void setText(String text) {
		this.textInputBox.setString(text);
	}
	
	public boolean isSelected() {
		return textInputBox.isSelected();
	}
}
