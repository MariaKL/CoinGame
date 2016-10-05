package kaukau.model;

import java.io.Serializable;
import java.util.ArrayList;

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

	public boolean addCoin(Item item){
		if (item instanceof Coin){
			Coin coin = (Coin) item;
			if (storage.addItem(item)){
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

	public int totalCoins(){
		return totalCoinAmount;
	}

	/**
	 * Check if this coinbox is full or not.
	 * @return ArrayList <Collectable>
	 * */
	public boolean isStorageFull(){
		return storage.isStorageFull();
	}
}
