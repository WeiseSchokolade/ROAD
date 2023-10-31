package de.schoko.uitil;

import de.schoko.rendering.Graph;
import de.schoko.uitil.frame.EventButton;
import de.schoko.uitil.frame.Frame;
import de.schoko.uitil.frame.InteractionListener;
import de.schoko.uitil.frame.SimpleTextBox;

public class ConfirmDialogFrame extends Frame {
	private boolean completed;
	
	public ConfirmDialogFrame(String title, String message, int x, int y, InteractionListener complete) {
		super(title, x, y, Graph.getStringWidth(message, NAME_FONT) + 10, false,
				new SimpleTextBox(message));
		addButton(new EventButton("Yes", () -> {
			complete.call();
			completed = true;
		}));
		addButton(new EventButton("No", () -> {
			completed = true;
		}));
	}
	
	public boolean isCompleted() {
		return completed;
	}
}
