package kaukau.model;

import java.io.Serializable;

/**
 * A cPickupable object is something a player can pick up, inspect, store in a container or drop.
 * Pickupable items include Keys, pieces of maps, notes and clues.*/
public abstract class PickupableItem implements Item, Serializable{
	
	protected String name;
	
	public PickupableItem(String name){
		this.name = name;
	}
	/**
	 * Gets the item name
	 * @return The name of this item
	 * */
	public String getName(){
		return name;
	}
	
}
