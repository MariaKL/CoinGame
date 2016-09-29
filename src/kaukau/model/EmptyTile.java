package kaukau.model;

import kaukau.model.Room.TileType;

public class EmptyTile extends Tile{

	private PickupableItem item;
	private Player player;

	public EmptyTile(TileType type, int x, int y){
		super(TileType.EMPTY, x, y);
		item = null;
		player = null;
	}

	public boolean addItem(PickupableItem addItem){
		if (this.item != null || this.player != null) return false;
		this.item = addItem;
		return true;
	}

	public boolean addPlayer(Player player){
		if (this.player != null || this.item != null) return false;
		this.player = player;
		return true;
	}

	public boolean removePlayer(){
		if (this.player == null) return false;
		this.player = null;
		return true;
	}

	public boolean dropItem(PickupableItem dropItem){
		if (this.item != null || this.player != null) return false;
		this.item = dropItem;
		return true;
	}

	public boolean removeItem(){
		if (this.item == null) return false;
		this.item = null;
		return true;
	}

	public boolean containsItem(){
		if (this.item == null) return false;
		return true;
	}

	public boolean isTileOccupied(){
		if (player != null || item != null) return true;
		return false;
	}

	public PickupableItem getItem(){
		return item;
	}

}
