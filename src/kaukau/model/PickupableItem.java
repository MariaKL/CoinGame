package kaukau.model;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
/**
 * A Pickupable object is something a player can pick up, inspect, store in a container or drop.
 * Pickupable items include Keys, pieces of maps, notes and clues.
 * @author Vivienne Yapp, 300339524
 *
 */

@XmlRootElement

public abstract class PickupableItem implements Item, Serializable{

	protected String name;

	public PickupableItem(String name){
		this.name = name;
	}

	/**
	 * Return the name of this item.
	 * @return the name of this item
	 */
	public String getName(){
		return name;
	}

}
