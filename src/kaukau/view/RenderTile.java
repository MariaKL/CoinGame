package kaukau.view;

import kaukau.model.Item;

public class RenderTile extends Block {
	// Field for type of tile
	private int tileType;
	// field for the the item the tile holds or null if none
	private Item item = null;
	
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
	public void setItem(Item i){
		this.item = i;
	}

	/*
	 * Getter methods
	 */
	public int getTileType() { return tileType;	}
	public Item getItem() { 
		if(item!=null){ return item; }
		else { return null; }
	}
}
