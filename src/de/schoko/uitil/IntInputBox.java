package de.schoko.uitil;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import de.schoko.rendering.Context;
import de.schoko.rendering.Graph;
import de.schoko.rendering.Keyboard;
import de.schoko.rendering.Mouse;

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

	public IntInputBox(Context context, int x, int y, int defaultNum, int minNum, int maxNum) {
		this.mouse = context.getMouse();
		this.keyboard = context.getKeyboard();
		this.x = x;
		this.y = y;
		this.minWidth = 50;
		this.height = 20;
		this.num = defaultNum;
		this.minNum = minNum;
		this.maxNum = maxNum;
		this.font = new Font("Segoe UI", Font.PLAIN, this.height);
	}
	
	@Override
	public void call(Graphics2D g2D) {
		apply = false;
		if (this == InputBox.selectedInputBox) {
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
		stringWidth = Graph.getStringWidth("" + num, font);
		if (this == InputBox.selectedInputBox) {
			g2D.setColor(Graph.getColor(0, 255, 217));
			g2D.fillRect(x - 2, y - 2, getWidth() + 4, this.height + 14);
		}
		g2D.setColor(Color.WHITE);
		g2D.fillRect(x, y, getWidth(), this.height + 10);
		g2D.setColor(Color.BLACK);
		g2D.drawString("" + num, x, y + font.getSize());
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

	public boolean isSelected() {
		return (InputBox.selectedInputBox == this);
	}
	
	public void select() {
		InputBox.selectedInputBox = this;
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
