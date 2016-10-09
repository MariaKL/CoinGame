package kaukau.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

/**
 * This class represents a Coin object which can be pickup or drop by the player.
 * A coin can be stored in the player's inventory or in a CoinBox object.
 * @author Vivienne Yapp, 300339524
 *
 */
public class Coin extends PickupableItem implements Serializable{

	private int amount;

	/**
	 * Create a Coin object.
	 * @param amount the amount of this coin
	 */
	public Coin(int amount) {
		super("Coin");
		this.amount = amount;
	}

	@SuppressWarnings("unused")
	public Coin(){
		this(-1);
	}

	/**
	 * Get the amount of this coin.
	 * @return the amount of this coin
	 */
    @XmlElement(name="Coin")
	public int getAmount(){
		return amount;
	}
    
    public void setAmount(int amount){
    	this.amount = amount;
    }

}
