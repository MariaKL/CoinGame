package kaukau.model;

import java.io.Serializable;

public class Key extends PickupableItem implements Serializable{
	
	private Player owner;
	private int code;

	public Key(int code, String name) {
		super(name);
		this.code = code;
		owner = null;
	}
	
	public void setOwner(Player player){
		owner = player;
	}

	@Override
	public String[] getActions() {
		// TODO Auto-generated method stub
		return null;
	}

}
