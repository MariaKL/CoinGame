package kaukau.model;

import java.io.Serializable;


public class Player implements Serializable{

	private String name;
	private Tile location;
	private Bag bag;
	private Direction facing;
	private int userId;

	public Player (int uid, String name, Tile startLocation, Direction facing){
		this.userId = uid;
		if(name != null && startLocation != null){
			this.name = name;
			this.location = startLocation;
		}
		bag = new Bag(this.name+"'s Bag", this.location, this);
		this.facing = facing;
	}

	/**
	 * Sets the items location to be the argument
	 * */
	public void setLocation(Tile loc) {
		if(loc != null){
			this.location = loc;
			this.bag.setLocation(loc);
		}
		setFacingDirection(loc);
	}

	public void setFacingDirection(Tile loc){
		if (location.Y() == loc.Y()){
			if (location.X() - loc.X() == 1)
				facing = Direction.WEST;
			else facing = Direction.EAST;
		} else {
			if (location.Y() - loc.Y() == 1)
				facing = Direction.NORTH;
			else facing = Direction.SOUTH;
		}
	}

	public Direction facingDirection(){
		return facing;
	}

	/**
	 * Gets the item name
	 * @return String name
	 * */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the items location
	 * @return Tile*/
	public Tile getLocation() {
		return this.location;
	}

	/**
	 * adds item to players bag
	 * */
	public void addToBag(PickupableItem item){
		this.bag.addCollectable(item);
	}

	/**
	 * removes item from players bag
	 * */
	public void removeFromBag(PickupableItem item){
		this.bag.removeCollectable(item);
	}

	/**
	 * adds item to players bag
	 * */
	public Bag getBag(){
		return bag;
	}

	/**
	 * Returns the string description of this player.
	 * @return String
	 * */
	public String toString (){
		return "Player "+this.name+" at position "+this.location.toString();
	}
}
