package de.schoko.uitil;

import java.awt.Color;
import java.awt.Font;

import de.schoko.rendering.Context;
import de.schoko.rendering.Graph;
import de.schoko.rendering.HUDGraph;
import de.schoko.rendering.Keyboard;
import de.schoko.rendering.Mouse;
import de.schoko.uitil.appliers.StringApplier;

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
	private StringApplier applier;

	public TextInputBox(int x, int y, String defaultString, int characterLimit) {
		this(x, y, defaultString, characterLimit, null);
	}
	
	public TextInputBox(int x, int y, String defaultString, int characterLimit, StringApplier applier) {
		this.x = x;
		this.y = y;
		this.minWidth = 50;
		this.height = 20;
		this.string = defaultString;
		this.characterLimit = characterLimit;
		this.font = new Font("Segoe UI", Font.PLAIN, this.height);
		this.applier = applier;
	}
	
	@Override
	public void load() {
		Context context = getSystem().getContext();
		this.mouse = context.getMouse();
		this.keyboard = context.getKeyboard();
	}
	
	@Override
	protected void apply() {
		apply = true;
		if (applier != null) {
			applier.apply(string);
		}
	}
	
	@Override
	public void update() {
		apply = false;
		if (isSelected()) {
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
			if ((keyboard.wasRecentlyPressed(Keyboard.ESCAPE) || keyboard.wasRecentlyPressed(Keyboard.ENTER)) && isSelected()) {
				setSelectedInputBox(null);
			}
		}
		
		if (mouse.getScreenX() >= this.x - 2 && mouse.getScreenY() >= this.y - 2 && mouse.getScreenX() <= this.x + this.getWidth() + 4 && mouse.getScreenY() <= this.y + this.height + 14) {
			if (mouse.isPressed(Mouse.LEFT_BUTTON)) {
				setSelectedInputBox(this);
			}
		} else {
			if (isSelected()) {
				if (mouse.isPressed(Mouse.LEFT_BUTTON)) {
					setSelectedInputBox(null);
				}
			}
		}
	}
	
	@Override
	public void draw(HUDGraph hud) {
		stringWidth = Graph.getStringWidth(string, font);
		if (isSelected()) {
			hud.drawRect(x - 2, y - 2, getWidth() + 4, this.height + 14, Graph.getColor(0, 255, 217));
		}
		hud.drawRect(x, y, getWidth(), this.height + 10, Color.WHITE);
		hud.drawText(string, x, y + font.getSize(), Color.BLACK, font);
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

	public void select() {
		setSelectedInputBox(this);
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
