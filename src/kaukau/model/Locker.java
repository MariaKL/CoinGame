package kaukau.model;

/**Locker containers are found in some rooms and the players can store collectable items in there.
 * Each locker can only hold up to 8 items.
 * */
public class Locker extends Container{

	public Locker (String name, Tile loc){
		super(name, loc);
		setAmmount(8);
	}

	/**
	 * Returns a string describing the locker.
	 * */
	public String toString(){
		return "This is Locker: "+this.getName();
	}
}
