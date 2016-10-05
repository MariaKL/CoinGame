package kaukau.model;

import java.io.Serializable;

public class Door implements Item, Serializable{

	private Tile location;
	private final int key;
	private boolean locked;

	public Door(int key, Tile location){
		this.location = location;
		this.key = key;
		if (key > 0) this.locked = true;
		else this.locked = false;
	}

	/**
	 * returns whether or not door is locked
	 * @return boolean locked
	 * */
	public boolean isLocked(){
		return this.locked;
	}

	/**
	 * Locks or unlocks door if the key parameter matches the doors key
	 * @param int key
	 * */
	public void lockOrUnlock(int key){
		if(key == this.key){
			this.locked = !this.locked;
		}
	}

	public String toString(){
		return "Door at "+this.location.toString();
	}

	@Override
	public String getName() {
		return "Door";
	}


}
