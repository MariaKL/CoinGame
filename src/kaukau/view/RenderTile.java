package kaukau.view;

public class RenderTile extends Block {
	// Field for type of tile
	private int tileType;
	
	public RenderTile(int tileType, int x, int y) {
		super(x,y);
		this.tileType = tileType;
	}
	
	/*
	 * Setter methods
	 */
	public void setTileType(int tType){
		this.tileType = tType;
	}

	/*
	 * Getter methods
	 */
	public int getTileType() { return tileType;	}
}
