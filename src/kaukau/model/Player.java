package kaukau.model;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "Team_24.kaukau.model.GameWorld")
public class Player implements Serializable{
	@XmlElement
	private String name;
	//@XmlElement(name="location")
	private Tile location;
	//@XmlElement
	private Container inventory;
	@XmlElement
	private CoinBox coinbox;
	@XmlElement
	private Direction facing;
	@XmlElement
	private final int userId;

	public Player (int uid, String name, Tile startLocation, Direction facing){
		this.userId = uid;
		this.name = name;
		this.location = startLocation;
		this.facing = facing;
		inventory = new Container("Backpack", startLocation);
		inventory.setAmmount(8);
		this.coinbox = new CoinBox(this);
		this.inventory.addItem(this.coinbox);

	}

	@SuppressWarnings("unused")
	private Player() {
	this(-1, null, null, null);
	}

	/**
	 * Sets the items location to be the argument
	 * */
	public void setLocation(Tile loc) {
		if(loc != null){
			this.location = loc;
		}
		setFacingDirection(loc);
	}

	public void setFacingDirection(Tile loc){
		if (location.Y() == loc.Y()){
			if (location.X() - loc.X() == 1)
				facing = Direction.WEST;
			else facing = Direction.EAST;
		} else {
			if (location.Y() - loc.Y() == 1)
				facing = Direction.NORTH;
			else facing = Direction.SOUTH;
		}
	}

	public Direction facingDirection(){
		return facing;
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
	@XmlElement(name="location")
	public Tile getLocation() {
		return this.location;
	}

	/**
	 * adds item to players bag
	 * */
	public boolean addToBag(Item item){
		if (item instanceof Coin && !coinbox.isStorageFull())
			return coinbox.addCoin(item);
		else if (item instanceof CoinBox && coinbox != null) // player only allow one coinbox
			return false;
		return inventory.addItem(item);
	}

	/**
	 * removes item from players bag
	 * */
	public boolean removeFromBag(int index){
		Item item = inventory.getItem(index);
		if (item != null && item instanceof CoinBox)
			coinbox = null;
		return inventory.removeItem(index);
	}

	/**
	 * adds item to players bag
	 * */
	@XmlElementWrapper(name="inventory")
    @XmlElements({
    @XmlElement(name="item") }
    )
	public ArrayList <PickupableItem> getInventory(){
		return inventory.getStorage();
	}

	/**
	 * Return the user id of this player.
	 * @return the user id of this player.
	 */
	public int getUserId(){
		return userId;
	}

	/**
	 * Returns the total money in the player's coinBox if the player has one.
	 * @return total amount of coin or zero if there is not coinBox
	 * */
	public int totalMoney(){
		if (coinbox != null) return coinbox.totalCoins();
		return 0;
	}

	/**
	 * Return the size of the player's storage.
	 * @return the user id of this player.
	 */
	public int getStorageSize(){
		return inventory.getStorageLimit();
	}

	/**
	 * Returns the string description of this player.
	 * @return String
	 * */
	public String toString (){
		return "Player "+this.name+" at position "+this.location.toString();
	}

}
