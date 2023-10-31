package de.schoko.uitil.frame;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import de.schoko.rendering.Context;
import de.schoko.rendering.Graph;
import de.schoko.rendering.HUDGraph;
import de.schoko.rendering.Mouse;
import de.schoko.rendering.TextAlignment;

public abstract class Frame {
	public static final Font NAME_FONT = new Font("Consolas", Font.PLAIN, 20);
	public static final Color BACKGROUND_COLOR = Graph.getColor(35, 35, 35, 200);
	private Context context;
	private String name;
	private int x, y;
	private int width, height;
	private int horizontallMargin = 5, verticalMargin = 5;
	private boolean draggable;
	private boolean dragging;
	private int dragX, dragY;
	private ArrayList<Button> buttons;
	private boolean visible;
	private boolean mousePressedRecently;
	
	public Frame(String name, int x, int y, int width, boolean draggable, Button... buttons) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = 30; // Height of top thingie
		this.draggable = draggable;
		this.buttons = new ArrayList<>();
		for (int i = 0; i < buttons.length; i++) {
			this.buttons.add(buttons[i]);
			this.height += verticalMargin + buttons[i].getHeight();
		}
	}
	
	public void draw(HUDGraph hud) {
	}
	
	public final void internalDraw(HUDGraph hud) {
		if (!visible) return;
		
		hud.drawRect(x, y, width, this.height + verticalMargin, BACKGROUND_COLOR);
		hud.drawRect(x, y, width, 30, Graph.getColor(90, 90, 90));
		hud.drawText(name, x + width / 2, y + 20, Graph.getColor(255, 255, 255), NAME_FONT, TextAlignment.CENTER);
		
		int height = 30 + verticalMargin;
		for (int i = 0; i < buttons.size(); i++) {
			Button button = buttons.get(i);
			button.setX(this.x + horizontallMargin);
			button.setY(this.y + height);
			button.setWidth(this.width - horizontallMargin * 2);
			button.draw(hud);
			height += verticalMargin + button.getHeight();
		}
		
		this.draw(hud);
	}
	
	public void update(double deltaTimeMS) {
		
	}
	
	public final void internalUpdate(double deltaTimeMS) {
		if (!visible) return;
		
		Mouse mouse = context.getMouse();
		if (mouse.isPressed(Mouse.LEFT_BUTTON) && draggable) {
			if (!mousePressedRecently) {
				mousePressedRecently = true;
				if (touching(mouse.getScreenX(), mouse.getScreenY())) {
					if (mouse.getScreenY() <= y + 30) {
						dragging = true;
						dragX = (int) (x - mouse.getScreenX());
						dragY = (int) (y - mouse.getScreenY());
					}
				}
			} else if (dragging) {
				this.x = (int) (mouse.getScreenX() + dragX);
				this.y = (int) (mouse.getScreenY() + dragY);
			}
		} else {
			dragging = false;
			mousePressedRecently = false;
		}
		
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).setContext(getContext());
			buttons.get(i).update(deltaTimeMS);
		}
		
		this.update(deltaTimeMS);
	}
	
	public void clickEvent(int x, int y) {
		
	}
	
	public boolean touching(double x, double y) {
		return (visible) ? (x > this.x && y > this.y &&
				x < this.x + this.width && y < this.y + this.height) : false;
	}
	
	public void addButton(Button button) {
		this.buttons.add(button);
		this.height += verticalMargin + button.getHeight();
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public final void setContext(Context context) {
		this.context = context;
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).setContext(getContext());
		}
	}
	
	public final Context getContext() {
		return context;
	}
	
	public final ArrayList<Button> getButtons() {
		return buttons;
	}
}
