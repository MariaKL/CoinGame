package kaukau.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A container is an item able hold collectable type items.
 * Containers are lockers around the game and the bag's of each player*/
public class Container implements Item, Serializable{

	private String name;
	private int storageLimit;
	private ArrayList <PickupableItem> storage =  new ArrayList <PickupableItem>( );

	public Container (String name, Tile loc){
		if (name != null && loc != null){
			this.name = name;
		}
	}

	/**
	 * Protected method to set the max storage amount which is determined when the container is made.
	 * */
	protected void setAmmount (int ammount){
		if(ammount > 0){
			this.storageLimit = ammount;
		}
	}

	/**
	 * Adds a pickupable item to a container
	 * @throws NullPointer Exception
	 * */
	public boolean addItem(Item item){
		if (item != null && item instanceof PickupableItem){
			if (this.storage.size() < this.storageLimit){
				storage.add((PickupableItem) item);
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes a pickupable item from a container
	 * @throws NullPointer Exception
	 * */
	public boolean removeItem(Item item){
		if (item != null && storage.contains(item)){
			storage.remove((PickupableItem) item);
			return true;
		}
		return false;
	}

	/**
	 * Removes a pickupable item from this container using index number of ArrayList
	 * @throws NullPointer Exception
	 * */
	public boolean removeItem(int index){
		if (index < this.storage.size() && index >= 0){
			storage.remove(index);
			return true;
		}
		return false;
	}

	/**
	 * Returns the list of items in the container
	 * @return ArrayList <Collectable>
	 * */
	public ArrayList <PickupableItem> getStorage(){
		return this.storage;
	}

	/**
	 * Returns the limit size of this container
	 * @return ArrayList <Collectable>
	 * */
	public int getStorageLimit(){
		return this.storageLimit;
	}

}
