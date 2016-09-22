package kaukau.view;

public class Tile {
	
	// Field for type of tile
	private final int tileType;
	// Fields for x & y position of tile
	private final int x;
	private final int y;
	
	public Tile(int tileType, int x, int y) {
		this.tileType = tileType;
		this.x = x;
		this.y = y;
	}

	/*
	 * Getter methods
	 */
	public int getTileType() { return tileType;	}
	public int X() { return x; }
	public int Y() { return y; }

}
