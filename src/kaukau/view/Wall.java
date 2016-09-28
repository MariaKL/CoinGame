package kaukau.view;

public class Wall {

	// Field for type of tile
	private int wallType;
	// Fields for x & y position of tile
	private final int x;
	private final int y;
	
	public Wall(int wallType, int x, int y){
		this.wallType = wallType;
		this.x = x;
		this.y = y;
	}
	
	/*
	 * Setter methods
	 */
	
	public void setWallType(int wType){
		this.wallType = wType;
	}

	/*
	 * Getter methods
	 */
	public int getWallType() { return wallType;	}
	public int X() { return x; }
	public int Y() { return y; }

	
}
