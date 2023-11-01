package de.schoko.uitil;

import java.awt.Color;
import java.awt.Font;

import de.schoko.rendering.Context;
import de.schoko.rendering.Graph;
import de.schoko.rendering.HUDGraph;
import de.schoko.rendering.Keyboard;
import de.schoko.rendering.Mouse;
import de.schoko.uitil.appliers.IntApplier;

public class IntInputBox extends InputBox {
	private static final int[] ENTERABLE_CHARACTERS = {
			Keyboard.ZERO, Keyboard.ONE, Keyboard.TWO, Keyboard.THREE, Keyboard.FOUR,
			Keyboard.FIVE, Keyboard.SIX, Keyboard.SEVEN, Keyboard.EIGHT, Keyboard.NINE,
			Keyboard.BACK_SPACE};

	private static final char[] RESULTING_CHARACTERS = {
			'0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9',
			'/'
	};
	
	private Mouse mouse;
	private Keyboard keyboard;
	private int x, y;
	private int num;
	private int maxNum, minNum;
	private int stringWidth, height;
	private int minWidth;
	private Font font;
	private boolean apply;
	
	private IntApplier applier;
	
	public IntInputBox(int x, int y, int defaultNum, int minNum, int maxNum) {
		this(x, y, defaultNum, minNum, maxNum, null);
	}

	public IntInputBox(int x, int y, int defaultNum, int minNum, int maxNum, IntApplier applier) {
		this.x = x;
		this.y = y;
		this.minWidth = 50;
		this.height = 20;
		this.num = defaultNum;
		this.minNum = minNum;
		this.maxNum = maxNum;
		this.font = new Font("Segoe UI", Font.PLAIN, this.height);
		this.applier = applier;
	}

	@Override
	public void load() {
		Context context = getSystem().getContext();
		mouse = context.getMouse();
		keyboard = context.getKeyboard();
	}
	
	@Override
	protected void apply() {
		apply = true;
		if (applier != null) {
			applier.apply(num);
		}
	}
	
	@Override
	public void update() {
		apply = false;
		if (isSelected()) {
			for (int i = 0; i < ENTERABLE_CHARACTERS.length; i++) {
				if (keyboard.wasRecentlyPressed(ENTERABLE_CHARACTERS[i])) {
					if (RESULTING_CHARACTERS[i] == '/') {
						num /= 10;
						continue;
					}
					num *= 10;
					num += Integer.valueOf("" + RESULTING_CHARACTERS[i]);
					num = Math.max(Math.min(num, maxNum), minNum);
				}
			}
			if (keyboard.wasRecentlyPressed(Keyboard.UP)) {
				num += 1;
				num = Math.max(Math.min(num, maxNum), minNum);
			}
			if (keyboard.wasRecentlyPressed(Keyboard.DOWN)) {
				num -= 1;
				num = Math.max(Math.min(num, maxNum), minNum);
			}
			if (keyboard.wasRecentlyPressed(Keyboard.MINUS)) {
				num *= -1;
				num = Math.max(Math.min(num, maxNum), minNum);
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
		stringWidth = Graph.getStringWidth("" + num, font);
		if (isSelected()) {
			hud.drawRect(x - 2, y - 2, getWidth() + 4, this.height + 14, Graph.getColor(0, 255, 217));
		}
		hud.drawRect(x, y, getWidth(), this.height + 10, Color.WHITE);
		hud.drawText("" + num, x, y + font.getSize(), Color.BLACK, font);
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
	
	public int getNum() {
		return num;
	}

	public void select() {
		setSelectedInputBox(this);
	}

	public void setNum(int num) {
		this.num = num;
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
