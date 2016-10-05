package kaukau.model;

import java.io.Serializable;

public class Coin extends PickupableItem implements Serializable{

	private final int amount;

	public Coin(int amount) {
		super("Coin");
		this.amount = amount;
	}

	public int getAmount(){
		return amount;
	}

}
