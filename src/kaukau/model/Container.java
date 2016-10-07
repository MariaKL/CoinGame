package kaukau.model;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * /**
 * A container is an item able hold pickupable type items.
 * Containers are storage for a coinbox, player's inventory...etc.
 * @author
 *
 */

@XmlRootElement
public class Container implements Item, Serializable{

	private String name;
	private int storageLimit;
	private ArrayList <PickupableItem> storage =  new ArrayList <PickupableItem>( );

	public Container (String name, Tile loc){
		if (name != null && loc != null){
			this.name = name;
		}
	}

	@SuppressWarnings("unused")
	public Container(){
		this(null, null);
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
	 * Returns the item at this index.
	 * @return an item if index is within storage size, otherwise null
	 * */
	public Item getItem(int index){
		if (index >= storageLimit && index >= storage.size()) return null;
		return this.storage.get(index);
	}

	/**
	 * Returns the size of this container that occupy by this container.
	 * @return ArrayList <Collectable>
	 * */
	public int getStorageUsed(){
		return this.storage.size();
	}

	/**
	 * Returns the limit size of this container
	 * @return ArrayList <Collectable>
	 * */
	public int getStorageLimit(){
		return this.storageLimit;
	}

	/**
	 * Check if this container is full or not.
	 * @return
	 * */
	public boolean isStorageFull(){
		return storage.size() >= storageLimit;
	}

	@Override
	public String getName() {
		return name;
	}

}
