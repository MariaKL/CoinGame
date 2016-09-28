package kaukau.model;

import java.util.ArrayList;

/**
 * A collectable object is something a player can pick up, inspect, store in a container or drop.
 * Collectable items include Keys, pieces of maps, notes and clues.*/
public class PickupableItem implements Item{
	private Tile location;
	private String name;
	private String code;
	private Player owner;

	public PickupableItem (String name, Tile loc, Player owner){
		if (name != null && loc != null){
			this.name = name;
			this.location = loc;
		}
	}
	
	public PickupableItem (String name){
		this.name = name;
	}

	@Override
	public void setLocation(Tile loc) {
		if(loc != null){
			this.location = loc;
		}
	}

	@Override
	public Tile getLocation() {
		return this.location;
	}

	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the last player who has collected this item or null if it currently
	 * doesn't belong to anyone*/
	public Player getOwner(){
		return owner;
	}

	/**
	 * Sets current owner or null if no owner
	 * */
	public void setOwner(Player owner){
		this.owner = owner;
	}

}
