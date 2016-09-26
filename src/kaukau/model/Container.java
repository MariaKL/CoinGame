package kaukau.model;

import java.util.ArrayList;

/**
 * A container is an item able hold collectable type items.
 * Containers are lockers around the game and the bag's of each player*/
public class Container implements Item{
	private Tile location;
	private String name;
	private int storageLimit;
	private ArrayList <PickupableItem> storage =  new ArrayList <PickupableItem>( );

	public Container (String name, Tile loc){
		if (name != null && loc != null){
			this.name = name;
			this.location = loc;
		}
	}

	@Override
	public void setLocation(Tile loc) {
		if(loc != null){
			this.location = loc;
			//set the location for all items in container to be same as container.
			for(PickupableItem c : this.storage){
				c.setLocation(loc);
			}
		}
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Tile getLocation() {
		return this.location;
	}

	/**
	 * Protected method to set the max storage ammount which is determined when the container is made.
	 * */
	protected void setAmmount (int ammount){
		if(ammount > 0){
			this.storageLimit = ammount;
		}
	}

	/**
	 * Adds a collectable item to a container
	 * @throws NullPointer Exception
	 * */
	public void addCollectable(PickupableItem item){
		if(item != null){
			if(this.storage.size() < this.storageLimit){
				storage.add(item);
			}
		}
	}

	/**
	 * Removes a collectable item from a container
	 * @throws NullPointer Exception
	 * */
	public void removeCollectable(PickupableItem item){
		if(item != null){
			if(this.storage.contains(item)){
				storage.remove(item);
			}
		}
	}

	/**
	 * Returns the list of items in the container
	 * @return ArrayList <Collectable>
	 * */
	public ArrayList <PickupableItem> getStorage(){
		return this.storage;
	}

}
