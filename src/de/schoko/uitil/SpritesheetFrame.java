package de.schoko.uitil;

import de.schoko.rendering.Graph;
import de.schoko.rendering.Image;
import de.schoko.rendering.shapes.Shape;

public class SpritesheetFrame extends Shape {
	private double x, y;
	private Image image;
	private int columns, rows;
	private double scale;
	private int imageWidth, imageHeight;
	private int colWidth, rowHeight;
	private int currentRow, currentCol;
	
	public SpritesheetFrame(Image image, double scale, int columns, int rows) {
		this.image = image;
		this.columns = columns;
		this.rows = rows;
		this.scale = scale;
		this.imageWidth = (int) (image.getAWTImage().getWidth(null));
		this.imageHeight = (int) (image.getAWTImage().getHeight(null));
		this.colWidth = this.imageWidth / columns;
		this.rowHeight = this.imageHeight / rows;
	}
	
	@Override
	public void render(Graph g) {
		int dx1 = g.convSX(x);
		int dy1 = g.convSY(y + 1);
		int dx2 = dx1 + g.convSW(scale);
		int dy2 = dy1 + g.convSH(scale);
		int sx1 = currentCol * this.colWidth;
		int sy1 = currentRow * this.rowHeight;
		int sx2 = sx1 + this.colWidth;
		int sy2 = sy1 + this.rowHeight;
		
		g.getAWTGraphics().drawImage(image.getAWTImage(), dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
	}
	
	public void nextFrame() {
		this.currentCol++;
		if (this.currentCol >= this.columns) {
			this.currentCol = 0;
		}
	}
	
	public void setCurrentRow(int currentRow) {
		if (currentRow == this.currentRow || currentRow >= this.rows) {
			return;
		}
		this.currentRow = currentRow;
		this.currentCol = 0; // Reset animation every time frame changes
	}
	
	public int getCurrentRow() {
		return currentRow;
	}
	
	public int getCurrentCol() {
		return currentCol;
	}
	
	public int getColumns() {
		return columns;
	}
	
	public int getRows() {
		return rows;
	}

	public void updateXY(double x, double y) {
		this.x = x;
		this.y = y;
	}
}
