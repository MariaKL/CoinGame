package kaukau.model;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

/**
 * A CoinBox is a pickupable item which can be carry by player. This CoinBox
 * object can be stored in Player's inventory and CoinBox can used to store Coin
 * object only.
 *
 * @author Vivienne Yapp, 300339524
 *
 */
public class CoinBox extends PickupableItem implements Serializable {

	private Container storage;
	private Player player;
	private int totalCoinAmount;

	public CoinBox(Player player) {
		super("Coin Box");
		this.player = player;
		this.storage = new Container("Coin Box", player.getLocation());
		storage.setAmount(20);
		totalCoinAmount = 0;
	}

	@SuppressWarnings("unused")
	public CoinBox() {
		this(null);
	}

	/**
	 * Add coin to the coinbox.
	 *
	 * @param item
	 *            the item to add
	 * @return true if the item is a coin and the coinBox is not full, otherwise
	 *         false
	 */
	public boolean addCoin(Item item) {
		if (item instanceof Coin) {
			Coin coin = (Coin) item;
			if (storage.addItem(item) && !storage.isStorageFull()){
				totalCoinAmount += coin.getAmount();
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the list of coin in the coin box.
	 *
	 * @return return all the coin objects
	 */
//	@XmlElementWrapper(name = "getStorage")
//	@XmlElements({ @XmlElement(name = "coin") })
	@XmlElement(name = "coin")
	public ArrayList<Coin> getStorage() {
		ArrayList<Coin> coins = new ArrayList<Coin>();
		for (PickupableItem p : this.storage.getStorage()) {
			coins.add((Coin) p);
		}
		return coins;
		// return this.storage.getStorage();
	}

	/**
	 * Return the total coins in this coinBox.
	 *
	 * @return
	 */
	public int totalCoins() {
		return totalCoinAmount;
	}

	/**
	 * Check if this coin box is full or not.
	 *
	 * @return
	 */
	@XmlElement(name = "isFull")
	public boolean isStorageFull() {
		return storage.isStorageFull();
	}
}
