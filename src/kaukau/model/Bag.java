package kaukau.model;

import java.io.Serializable;

/**
 * Each Player has bag they carry with them. It is a container that can hold up to 5 collectable items*/
public class Bag extends Container implements Serializable{
	private Player player;
	
	public Bag (String name, Tile loc, Player player){
		super(name, loc);
		this.player = player;
		setAmmount(5);
	}
	
	/**
	 * Returns the player who this bag belongs to
	 * @return Player
	 * */
	public Player getPlayer(){
		return this.player;
	}
	
	/**
	 * Returns a string describing whos bag it it.
	 * */
	public String toString(){
		return "This is the bag("+this.getName()+") of "+this.player.getName()+".";
	}
}
