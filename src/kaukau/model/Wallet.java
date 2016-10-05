package kaukau.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Wallet implements Item, Serializable{
	
	private int storageLimit;
	private ArrayList <PickupableItem> storage =  new ArrayList <PickupableItem>( );
	private Player player;
	
	public Wallet(Player player){
		this.player = player;
	}
	
	/**
	 * Protected method to set the max storage amount which is determined when the container is made.
	 * */
	protected void setAmmount (int amount){
		if (amount > 0) {
			this.storageLimit = amount;
		}
	}
	
	/**
	 * Adds a coin to the player's wallet.
	 * @throws NullPointer Exception
	 * */
	public boolean addItem(PickupableItem item){
		if (item instanceof Coin){
			if (this.storage.size() < this.storageLimit){
				storage.add(item);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removes an item from the wallet
	 * @throws NullPointer Exception
	 * */
	public boolean removeItem(PickupableItem item){
		if (item != null && storage.contains(item)){
			if (this.storage.contains(item)){
				storage.remove(item);
				return true;
			}
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

}
