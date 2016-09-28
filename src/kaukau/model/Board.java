package kaukau.model;

/**
 * Board class represents the board as a 2d array of Tiles.*/
public class Board {
	private Tile [][] board;
	private static final int SIZE = 7;

	public Board (){
		board = new Tile [SIZE][SIZE];
	}

	public void addTile(Tile t, int row, int col){
		if(board[row][col] == null){
			board[row][col] = t;
		}
	}

	public void draw(){
		for(int i = 0; i < board.length; i++ ){
			for(int j = 0; j < board[i].length; j++ ){
				System.out.println(board[i][j].getCode());
			}
		}
	}



}
