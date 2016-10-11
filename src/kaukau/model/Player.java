package kaukau.model;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement // (namespace = "Team_24.kaukau.model.GameWorld")
@XmlType(propOrder = { "name", "userId", "facingDirection", "location", "inventory"})
public class Player implements Serializable {
	@XmlElement
	private String name;
	private Tile location;
	private Container inventory;
	private CoinBox coinbox;
	private Direction facing;
	@XmlElement
	private final int userId;

	public Player(int uid, String name, Tile startLocation, Direction facing) {
		this.userId = uid;
		this.name = name;
		location = startLocation;
		this.facing = facing;
		// inventory = new Container("Backpack", startLocation);
		inventory = new Container("Backpack");
		inventory.setAmount(8);
		this.coinbox = new CoinBox(this);
		this.inventory.addItem(this.coinbox);
		this.coinbox.setPlayer(this);
	}

	@SuppressWarnings("unused")
	private Player() {
		this(-1, null, null, null);
	}

	/**
	 * Sets the items location to be the argument
	 */
	public void setLocation(Tile loc) {
		this.location = loc;
	}

	public void setfacingDirection(Direction direct) {
		this.facing = direct;
	}

	@XmlElement(name = "getFacingDirection")
	public Direction getfacingDirection() {
		return facing;
	}

	/**
	 * Gets the item name
	 *
	 * @return String name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the items location
	 *
	 * @return Tile
	 */
	@XmlElement(name = "getLocation")
	public Tile getLocation() {
		return this.location;
	}

	/**
	 * Return the coinbox that belong to this player.
	 *
	 * @return a CoinBox type object, otherwise null if player drop it.
	 */
	public CoinBox getCoinBox() {
		return this.coinbox;
	}

	/**
	 * adds item to players bag
	 */
	public boolean addToBag(Item item) {
		if (item instanceof Coin && !coinbox.isStorageFull())
			return coinbox.addCoin(item);
		else if (item instanceof CoinBox && coinbox != null) // player only
																// allow one
																// coinbox
			return false;
		return inventory.addItem(item);
	}

	/**
	 * removes item from players bag
	 */
	public boolean removeFromBag(int index) {
		Item item = inventory.getItem(index);
		if (item != null && item instanceof CoinBox)
			coinbox = null;
		return inventory.removeItem(index);
	}

	/**
	 * adds item to players bag
	 */
	@XmlElementWrapper(name = "inventory")
	@XmlElements({ @XmlElement(name = "item") })
	public ArrayList<PickupableItem> getInventory() {
		return inventory.getStorage();
	}

	public void setInventory(ArrayList<PickupableItem> inv) {
		inventory.setStorage(inv);
	}

	/**
	 * Return the user id of this player.
	 *
	 * @return the user id of this player.
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * Returns the total money in the player's coinBox if the player has one.
	 *
	 * @return total amount of coin or zero if there is not coinBox
	 */
	public int totalMoney() {
		if (coinbox != null)
			return coinbox.totalCoins();
		return 0;
	}

	/**
	 * Return the size of the player's storage.
	 *
	 * @return the user id of this player.
	 */
	public int getStorageSize() {
		return inventory.getStorageLimit();
	}

	/**
	 * Returns the string description of this player.
	 *
	 * @return String
	 */
	public String toString() {
		return "Player " + this.name + " at position " + this.location.toString();
	}

	public boolean containsKey(Door door){
		for(PickupableItem item : inventory.getStorage()){
			if (item instanceof Key){
				if (((Key) item).getCode() == door.getDoorCode()){
					return true;
				}
			}
		}
		return false;
	}

}
