package kaukau.model;

import java.awt.Graphics;
import java.io.Serializable;

/**
 * Item interface is the backbone of all the items in the game the player can interact with
 * ie collectible objects, puzzles, clues, doors and containers such as the players bag and lockers.*/
public interface Item extends Serializable{

	/**
	 * Return the name of this item.
	 * @return the name of this item
	 */
	public String getName();
}
