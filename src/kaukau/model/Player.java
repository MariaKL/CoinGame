package kaukau.model;

import java.io.Serializable;
import java.util.ArrayList;


public class Player implements Serializable{

	private String name;
	private Tile location;
	private Container inventory;
	private Direction facing;
	private final int userId;

	public Player (int uid, String name, Tile startLocation, Direction facing){
		this.userId = uid;
		this.name = name;
		this.location = startLocation;
		this.facing = facing;
		inventory = new Container("Backpack", startLocation);
		inventory.setAmmount(5);
	}

	/**
	 * Sets the items location to be the argument
	 * */
	public void setLocation(Tile loc) {
		if(loc != null){
			this.location = loc;
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
	public boolean addToBag(Item item){
		return inventory.addItem(item);
	}

	/**
	 * removes item from players bag
	 * */
	public boolean removeFromBag(int index){
		return inventory.removeItem(index);
	}

	/**
	 * adds item to players bag
	 * */
	public ArrayList <PickupableItem> getInventory(){
		return inventory.getStorage();
	}
	
	/**
	 * Return the user id of this player.
	 * @return the user id of this player.
	 */
	public int getUserId(){
		return userId;
	}
	
	/**
	 * Return the size of the player's storage.
	 * @return the user id of this player.
	 */
	public int getStorageSize(){
		return inventory.getStorageLimit();
	}

	/**
	 * Returns the string description of this player.
	 * @return String
	 * */
	public String toString (){
		return "Player "+this.name+" at position "+this.location.toString();
	}
}
