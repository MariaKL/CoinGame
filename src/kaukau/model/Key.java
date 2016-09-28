package kaukau.model;

public class Key extends PickupableItem{
	
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

}
