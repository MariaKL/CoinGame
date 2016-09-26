package kaukau.model;

public class Player {
	private String name;
	private Tile location;
	private Bag bag;

	public Player (String name, Tile startLocation){
		if(name != null && startLocation != null){
			this.name = name;
			this.location = startLocation;
		}
		bag = new Bag(this.name+"'s Bag", this.location, this);
	}

	/**
	 * Sets the items location to be the argument
	 * */
	public void setLocation(Tile loc) {
		if(loc != null){
			this.location = loc;
			this.bag.setLocation(loc);
		}
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
	 * Returns the string description of this player.
	 * @return String
	 * */
	public String toString (){
		return "Player "+this.name+" at position "+this.location.toString();
	}
}
