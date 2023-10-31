package de.schoko.uitil;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import de.schoko.rendering.Context;
import de.schoko.rendering.Graph;
import de.schoko.rendering.Keyboard;
import de.schoko.rendering.Mouse;

public class TextInputBox extends InputBox {
	private static final int[] ENTERABLE_CHARACTERS = {
			Keyboard.A, Keyboard.B, Keyboard.C, Keyboard.D, Keyboard.E,
			Keyboard.F, Keyboard.G, Keyboard.H, Keyboard.I, Keyboard.J,
			Keyboard.K, Keyboard.L, Keyboard.M, Keyboard.N, Keyboard.O,
			Keyboard.P, Keyboard.Q, Keyboard.R, Keyboard.S, Keyboard.T,
			Keyboard.U, Keyboard.V, Keyboard.W, Keyboard.X, Keyboard.Y,
			Keyboard.Z,
			Keyboard.ZERO, Keyboard.ONE, Keyboard.TWO, Keyboard.THREE, Keyboard.FOUR,
			Keyboard.FIVE, Keyboard.SIX, Keyboard.SEVEN, Keyboard.EIGHT, Keyboard.NINE,
			Keyboard.BACK_SPACE, Keyboard.SPACE, Keyboard.SEMICOLON, Keyboard.UNDERSCORE,
			Keyboard.ALT, Keyboard.PERIOD};

	private static final char[] RESULTING_CHARACTERS = {
			'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o',
			'p', 'q', 'r', 's', 't',
			'u', 'v', 'w', 'x', 'y',
			'z',
			'0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9',
			'/', ' ', ';', '_',
			';', '.'
	};
	
	private Mouse mouse;
	private Keyboard keyboard;
	private int x, y;
	private String string;
	private int stringWidth, height;
	private int minWidth;
	private int characterLimit;
	private Font font;
	private boolean apply;

	public TextInputBox(Context context, int x, int y, String defaultString, int characterLimit) {
		this.mouse = context.getMouse();
		this.keyboard = context.getKeyboard();
		this.x = x;
		this.y = y;
		this.minWidth = 50;
		this.height = 20;
		this.string = defaultString;
		this.characterLimit = characterLimit;
		this.font = new Font("Segoe UI", Font.PLAIN, this.height);
	}

	@Override
	public void call(Graphics2D g2D) {
		apply = false;
		if (this == InputBox.selectedInputBox) {
			for (int i = 0; i < ENTERABLE_CHARACTERS.length; i++) {
				if (keyboard.wasRecentlyPressed(ENTERABLE_CHARACTERS[i])) {
					if (RESULTING_CHARACTERS[i] == '/') {
						if (string.length() == 0) continue;
						string = string.substring(0, string.length() - 1);
						continue;
					}
					if (string.length() >= characterLimit) continue;
					if (keyboard.isPressed(Keyboard.SHIFT)) {
						string += Character.toString(RESULTING_CHARACTERS[i]).toUpperCase();
					} else {
						string += Character.toString(RESULTING_CHARACTERS[i]);
					}
				}
			}
			if ((keyboard.wasRecentlyPressed(Keyboard.ESCAPE) || keyboard.wasRecentlyPressed(Keyboard.ENTER)) && this == InputBox.selectedInputBox) {
				apply = true;
				InputBox.selectedInputBox = null;
			}
		} else {
			if (mouse.getScreenX() >= this.x && mouse.getScreenY() >= this.y && mouse.getScreenX() <= this.x + this.getWidth() && mouse.getScreenY() <= this.y + this.height) {
				if (mouse.isPressed(Mouse.LEFT_BUTTON)) {
					InputBox.selectedInputBox = this;
				}
			}
		}
		g2D.setFont(font);
		stringWidth = Graph.getStringWidth(string, font);
		if (this == InputBox.selectedInputBox) {
			g2D.setColor(Graph.getColor(0, 255, 217));
			g2D.fillRect(x - 2, y - 2, getWidth() + 4, this.height + 14);
		}
		g2D.setColor(Color.WHITE);
		g2D.fillRect(x, y, getWidth(), this.height + 10);
		g2D.setColor(Color.BLACK);
		g2D.drawString(string, x, y + font.getSize());
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Font getFont() {
		return font;
	}

	public int getWidth() {
		return Math.max(stringWidth, minWidth);
	}
	
	public String getString() {
		return string;
	}

	public boolean isSelected() {
		return (InputBox.selectedInputBox == this);
	}
	
	public void select() {
		InputBox.selectedInputBox = this;
	}

	public void setString(String string) {
		this.string = string;
	}
	
	/**
	 * Whether the TextInputBox was exited.
	 * Marks the apply flag as false when called.
	 * @return
	 */
	public boolean shouldApply() {
		if (apply) {
			apply = false;
			return true;
		}
		return apply;
	}
}
