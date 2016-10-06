package kaukau.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A CoinBox is a pickupable item which can be carry by player.
 * This CoinBox object can be stored in Player's inventory and
 * CoinBox can used to store Coin object only.
 * @author Vivienne Yapp, 300339524
 *
 */
public class CoinBox extends PickupableItem implements Serializable{

	private Container storage;
	private Player player;
	private int totalCoinAmount;

	public CoinBox(Player player){
		super("Coin Box");
		this.player = player;
		this.storage = new Container("Coin Box", player.getLocation());
		storage.setAmmount(20);
		totalCoinAmount = 0;
	}

	/**
	 * Add coin to the coinbox.
	 * @param item the item to add
	 * @return true if the item is a coin and the coinBox is not full, otherwise false
	 */
	public boolean addCoin(Item item){
		if (item instanceof Coin){
			Coin coin = (Coin) item;
			if (storage.addItem(item) && !storage.isStorageFull()){
				totalCoinAmount = coin.getAmount();
			}
		}
		return false;
	}

	/**
	 * Returns the list of coin in the coin box.
	 * @return return all the coin objects
	 */
	public ArrayList <PickupableItem> getStorage(){
		return this.storage.getStorage();
	}

	/**
	 * Return the total coins in this coinBox.
	 * @return
	 */
	public int totalCoins(){
		return totalCoinAmount;
	}

	/**
	 * Check if this coin box is full or not.
	 * @return
	 */
	public boolean isStorageFull(){
		return storage.isStorageFull();
	}
}
