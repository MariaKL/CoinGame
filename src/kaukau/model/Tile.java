package kaukau.model;

public class Tile {
	private Item item;
	private float row, col;
	private char code;

	public Tile (float row, float col, char code){
		this.code = code;
		inspectCode();
		this.row = row;
		this.col = col;
	}

	private void inspectCode(){
		if (this.code == 'W'){
			this.item = null;
		}else if (this.code == 'C'){
			this.item = null;
		}else if (this.code == 'D'){
			this.item = new Door("Locked Door", 123, true);
		}else if (this.code == 'P'){
			this.item = new Player("Player 1", this);
		}else if (this.code == 'N'){
			this.item = new Note("You're fucked", this, null, 1);
		}
	}

	public String getCode(){
		return "" + this.code;
	}

	public String toString(){
		return "row = "+this.row+", col = "+this.col;
	}

}
