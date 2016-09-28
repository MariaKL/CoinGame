package kaukau.model;

import java.util.ArrayList;

/**
 * Note item is a collectable item that holds information such as clues and drawings of a
 * certain page in from a notebook. The more pages the player collects, the more clues they accumulate.*/
public class Note extends PickupableItem{
	private ArrayList <Note> noteBook = new ArrayList <Note> ();
	private int pageNumber;

	public Note (String name, Tile loc, Player player, int page){
		super(name, loc, player);
		//this.noteBook = pages;
		this.pageNumber = page;
	}

	/**
	 * Returns the page number
	 * */
	public int pageNumber (){
		return this.pageNumber;
	}

	/**
	 * Returns a string describing the note.
	 * */
	public String toString(){
		return "This is note is page "+this.pageNumber+"of "+this.noteBook.size()+ " pages of the notebook "+this.getName()+".";
	}

	@Override
	public String[] getActions() {
		// TODO Auto-generated method stub
		return null;
	}
}
