package kaukau.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import kaukau.model.GameMap.TileType;

@XmlRootElement
public class Tile implements Serializable {

	private Item item;
	private Key itemIsKey;
	private CoinBox itemIsCoinBox;
	private Coin itemIsCoin;
	private Player player;
	private TileType type;
	private int x, y;

	public Tile(TileType type, int x, int y) {
		this.type = type;
		this.x = x;
		this.y = y;
		item = null;
		player = null;
	}

	public Tile() {
		item = null;
		player = null;
	}

	/**
	 * Set this tile with a game item. This method mostly use to set up the game
	 * board.
	 *
	 * @param addItem
	 *            the item to add on this tile
	 * @return true if it is successfully added, otherwise false;
	 */
	public boolean setItem(Item addItem) {
		if (item == null && player == null) {
			this.item = addItem;
			checkType();
			return true;
		}
		return false;
	}

	/**
	 * Add a player on this empty tile.
	 *
	 * @param player
	 *            the player that occupy this tile
	 * @return true if the player successfully added, otherwise false.
	 */
	public boolean addPlayer(Player player){
		if (this.player == null && this.item == null){
			this.player = player;
			return true;
		}
		return false;
	}

	/**
	 * Remove the player that occupy this tile.
	 *
	 * @return true if the player successfully removed, otherwise false.
	 */
	public boolean removePlayer(){
		if (this.player != null){
			this.player = null;
			return true;
		}
		return false;
	}

	/**
	 * Returns the player currently on this tile or null if none
	 * @return
	 */
	public Player getPlayer(){
		if(this.player != null){
			return player;
		} else {
			return null;
		}
	}


	/**
	 * Drop an item to an empty tile if the tile is not occupy.
	 *
	 * @param dropItem
	 *            the item that drop on this tile
	 * @return true if both the player and item fields are NULL, otherwise
	 *         false.
	 */

	public boolean dropItem(PickupableItem dropItem){
		if ((type == TileType.TILE || type == TileType.TILE_CRACKED) && 
			item == null && player == null && dropItem instanceof PickupableItem){
			this.item = dropItem;
			checkType();
			return true;
		}
		return false;
	}

	/**
	 * Remove an item from a tile when a player pick the item.
	 *
	 * @return true if the tile is TileType.Empty and both the player and item
	 *         fields are NULL, otherwise false .
	 */
	public boolean removeItem(){
		if ((type == TileType.TILE || type == TileType.TILE_CRACKED) && item != null){
			this.item = null;
			checkType();
			return true;
		} 
		return false;
	}

	/**
	 * Check if this tile contain an pickupable item.
	 *
	 * @return true if there is a pickupableItem on an empty tile, otherwise
	 *         false.
	 */
	public boolean containsPickupItem(){
		if ((type == TileType.TILE || type == TileType.TILE_CRACKED) && 
				item != null && item instanceof PickupableItem){
			return true;
		}
		return false;
	}

	private void checkType() {
		if (this.item instanceof Key && this.item != null) {
			this.itemIsKey = (Key) this.item;
		} else if (this.item instanceof CoinBox && this.item != null) {
			this.itemIsCoinBox = (CoinBox) this.item;
		} else if (this.item instanceof Coin && this.item != null) {
			this.itemIsCoin = (Coin) this.item;
		} else if (this.item == null) {
			this.itemIsKey = null;
			this.itemIsCoinBox = null;
			this.itemIsCoin = null;
		}

	}

	@XmlElement(name = "getKey")
	public Key getKey(){
		if (this.itemIsKey != null) return this.itemIsKey;
		return null;
	}

	@XmlElement(name = "getCoinBox")
	public CoinBox getCoinBox(){
		if (this.itemIsCoinBox != null) return this.itemIsCoinBox;
		return null;
	}

	@XmlElement(name = "getCoin")
	public Coin getCoin(){
		if (this.itemIsCoin != null) return this.itemIsCoin;
		return null;
	}

	/**
	 * Check whether if this tile is occupy by a player or pickableItem.
	 *
	 * @return true if the tile is TileType.Empty, otherwise false if both the
	 *         player and item fields are NULL.
	 */
	public boolean isTileOccupied(){
		if (type == TileType.TILE || type == TileType.TILE_CRACKED){
			if (player == null && item == null){
				return false;
			}
		}
		return true;
	}

	/**
	 * Return the type of this tile.
	 *
	 * @return the type of this tile
	 */
	@XmlElement(name = "tileType")
	public TileType getTileType() {
		return type;
	}

	/**
	 * Return the item that occupy this tile.
	 *
	 * @return an item if it is pickupable item and there is one, otherwise
	 *         returns null.
	 */
	@XmlAnyElement
	public Item getItem() {
		if (item instanceof PickupableItem)
			return item;
		return null;
	}

	/**
	 * Return the column number of this tile.
	 *
	 * @return the column number
	 */
	public int X() {
		return x;
	}

	@XmlElement(name = "xCoord")
	public int getX() {
		return x;
	}

	/**
	 * Return the row number of this tile.
	 *
	 * @return the row number
	 */
	public int Y() {
		return y;
	}

	/**
	 * Return the row number of this tile.
	 *
	 * @return the row number
	 */
	@XmlElement(name = "yCoord")
	public int getY() {
		return y;
	}

	public String toString() {
		return "row = " + this.x + ", col = " + this.y;
	}

}
