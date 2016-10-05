package kaukau.model;

import java.io.Serializable;

public class Key extends PickupableItem implements Serializable{
	
	private final int code;

	public Key(int key) {
		super("Key");
		this.code = key;
	}

	public int getCode(){
		return code;
	}

}
