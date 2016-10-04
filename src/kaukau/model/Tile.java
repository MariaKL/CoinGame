package kaukau.model;

import java.io.Serializable;

import kaukau.model.Room.TileType;

public class Tile implements Serializable {

	private Item item;
	private Player player;
	private TileType type;
	private int x, y;

	public Tile(TileType type, int x, int y){
		this.type = type;
		this.x = x;
		this.y = y;
		item = null;
		player = null;
	}

	public boolean setItem(PickupableItem addItem){
		if (type != TileType.EMPTY) return false;
		else {
			if (item == null && player == null){
				item = addItem;
				return true;
			}
		}
		return false;
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
		if (type != TileType.EMPTY) return false;
		else {
			if (item == null && player == null){
				item = dropItem;
				return true;
			}
		}
		return false;
	}

	public boolean removeItem(){
		if (type != TileType.EMPTY) return false;
		else {
			if (item == null) return false;
			else item = null;
		}
		return false;
	}

	public boolean containsPickupItem(){
		if (type != TileType.EMPTY) return false;
		else {
			if (type == TileType.EMPTY && item instanceof PickupableItem){
				return true;
			}
		}
		return false;
	}

	public boolean isTileOccupied(){
		if (type != TileType.EMPTY)
			return true;
		else if (type == TileType.EMPTY && player == null && item == null)
			return false;
		else return true;
	}

	public String toString(){
		return "row = "+this.x+", col = "+this.y;
	}

	public TileType getTileType() { return type; }
	public int X() { return x; }
	public int Y() { return y; }

}
