package kaukau.model;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

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
		FileReader fr = new FileReader(filename);
		BufferedReader br = new BufferedReader(fr);
		ArrayList<String> lines = new ArrayList<String>();
		int width = -1;
		String line;
		while((line = br.readLine()) != null) {
			lines.add(line);

			// now sanity check

			if(width == -1) {
				width = line.length();
			} else if(width != line.length()) {
				throw new IllegalArgumentException("Input file \"" + filename + "\" is malformed; line " + lines.size() + " incorrect width.");
			}
		}

		Board board = new Board(width,lines.size());
		for(int y=0;y!=lines.size();++y) {
			line = lines.get(y);
			for(int x=0;x!=width;++x) {
				char c = line.charAt(x);
				switch (c) {
					case 'W' :
						board.addWall(x, y);
						break;
					case 'P':
						board.addPill(x, y);
						break;
					case 'X':
						board.registerPacPortal(x, y);
						break;
					case 'G':
						board.registerGhostPortal(x,y);
						break;
				}
			}
		}

		for(int i=0;i!=nHomerGhosts;++i) {
			board.registerGhost(true);
		}
		for(int i=0;i!=nRandomGhosts;++i) {
			board.registerGhost(false);
		}

		return board;
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
