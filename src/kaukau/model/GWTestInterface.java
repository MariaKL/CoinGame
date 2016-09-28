package kaukau.model;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import pacman.ui.Board;

public class GWTestInterface implements KeyListener {
	private Board board;
	private Player player;

	public GWTestInterface() {
		this.board = new Board();
		loadBoard();
		this.player = this.board.getPlayer();
	}

	private void loadBoard(){
		File file = new File("Tile.txt");
	    try {
	        Scanner sc = new Scanner(file);
	        int row = 0;
	        while (sc.hasNextLine()) {
	            String i = sc.next();
	            this.board.addTile(new Tile(), row, col);
	        }
	        sc.close();
	    }
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	}

	private void redraw() {
		this.board.draw();
		drawPlayerActions();
	}

	private void drawPlayerActions() {
		System.out.println("Use ARROW keys to move or SPACE to interact.");
	}

	public static void main(String[] args) {
		GWTestInterface game = new GWTestInterface();
		game.redraw();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			this.board.moveUp(this.player);
			redraw();
		}else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			this.board.player.moveDown(this.player);
			redraw();
		}else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			this.board.player.moveRight(this.player);
			redraw();
		}else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			this.board.player.moveLeft(this.player);
			redraw();
		}else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			this.board.player.collect(this.player);
			redraw();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
