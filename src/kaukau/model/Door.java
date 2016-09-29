package kaukau.model;

public class Door implements Item{
	private Tile location;
	private String name;
	private int key;
	private boolean locked;

	public Door(String name, int key, boolean locked){
		if(name != null && key > 0){
			this.name = name;
			this.key = key;
			this.locked = locked;
		}
	}

	@Override
	public void setLocation(Tile loc) {
		if(loc != null){
			this.location = loc;
		}
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Tile getLocation() {
		return this.location;
	}

	/**
	 * returns whether or not door is locked
	 * @return boolean locked
	 * */
	public boolean isLocked(){
		return this.locked;
	}

	/**
	 * Locks or unlocks door if the key parameter matches the doors key
	 * @param int key
	 * */
	public void lockOrUnlock(int key){
		if(key == this.key){
			this.locked = !this.locked;
		}
	}

	public String toString(){
		return "Door at "+this.location.toString();
	}
}
