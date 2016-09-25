package kaukau.view;

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
}
