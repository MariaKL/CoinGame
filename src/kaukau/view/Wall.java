package kaukau.view;

public class Wall extends Block {

	// Field for type of tile
	private int wallType;
	
	public Wall(int wallType, int x, int y){
		super(x,y);
		this.wallType = wallType;
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
}
