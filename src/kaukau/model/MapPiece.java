package kaukau.model;

import java.util.ArrayList;

/**
 * The MapPiece is a collectable item that which gives information of one piece of a whole map
 * It also holds information on which other map item pieces its a part of.
 * */
public class MapPiece extends PickupableItem{
	private ArrayList <MapPiece> mapPieces = new ArrayList <MapPiece> ();
	private int pieceNumber;

	public MapPiece (String name, Tile loc, Player player, ArrayList <MapPiece> pieces, int piece){
		super(name, loc, player);
		this.mapPieces = pieces;
		this.pieceNumber = piece;
	}

	/**
	 * Returns the piece number
	 * */
	public int pieceNumber (){
		return this.pieceNumber;
	}

	/**
	 * Returns a string describing the MapPiece.
	 * */
	public String toString(){
		return "This is piece "+this.pieceNumber+"of "+this.mapPieces.size()+ " pieces of map "+this.getName()+".";
	}

	@Override
	public String[] getActions() {
		// TODO Auto-generated method stub
		return null;
	}
}
