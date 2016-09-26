package kaukau.view;

import java.awt.Point;

public class Sprite {
	// Field for type of sprite
	private final int spriteType;
	// Fields for x & y position of tile
	int x;
	int y;
	
	public Sprite(int spriteType, int x, int y) {
		this.spriteType = spriteType;
		this.x = x;
		this.y = y;
	}

	/*
	 * Getter methods
	 */
	public int getSpriteType() { return spriteType;	}
	public int X() { return x; }
	public int Y() { return y; }
	public Point getTilePos(){
		Point p = RenderWindow.isoTo2D(new Point(this.x, this.y));
		Point result = RenderWindow.getTileCoordinates(p, 50);
		return result;
	}
	public void setPosFromTilePos(Point p) {
		Point twoD = RenderWindow.get2dFromTileCoordinates(p, 50);
		Point pos = RenderWindow.twoDToIso(twoD);
		this.x = pos.x;
		this.y = pos.y;
	}
}
