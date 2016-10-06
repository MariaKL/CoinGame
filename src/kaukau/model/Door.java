package kaukau.model;

import java.io.Serializable;

/**
 * This class represent a Door object that can placed on GameMap's tile.
 * @author Vivienne Yapp, 300339524
 *
 */
public class Door implements Item, Serializable{

	private Tile location;
	private final int keyCode;
	private boolean locked;

	/**
	 * Create a door with a key code and the location of the door
	 * @param key code to match with a key if it is locked
	 * @param location the location of this door
	 */
	public Door(int key, Tile location){
		this.location = location;
		this.keyCode = key;
		if (keyCode > 0) this.locked = true;
		else this.locked = false;
	}

	/**
	 * Check if this door is locked or not
	 * @return true if it is locked, otherwise false.
	 */
	public boolean isLocked(){
		return this.locked;
	}

	/**
	 * Check if the player can used this key to open a door
	 * @param item the key object
	 * @return true if the item is a Key type and the door is locked, otherwise false
	 */
	public boolean lockOrUnlock(Item item){
		if (item instanceof Key && locked){
			Key key = (Key) item;
			if (keyCode == key.getCode()){
				locked = false;
				return true;
			}
		}
		return false;
	}

	/**
	 * Return the door code that need to match with a key.
	 * @return the door code of this door
	 */
	public int getDoorCode(){
		return keyCode;
	}

	public String toString(){
		return "Door at "+this.location.toString();
	}

	/**
	 * Return the name of this item.
	 * @return the name of this item
	 */
	@Override
	public String getName() {
		return "Door";
	}


}
