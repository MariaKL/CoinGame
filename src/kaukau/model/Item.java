package kaukau.model;

import java.io.Serializable;

/**
 * Item interface is the backbone of all the items in the game the player can interact with
 * ie collectible objects, puzzles, clues, doors and containers such as the players bag and lockers.*/
public interface Item extends Serializable{

	/**
	 * Sets the items location to be the argument
	 * */
	public void setLocation(Tile loc);

	/**
	 * Gets the items location
	 * @return Tile*/
	public Tile getLocation();

	/**
	 * Gets the item name
	 * @return The name of this item
	 * */
	public String getName();

}
