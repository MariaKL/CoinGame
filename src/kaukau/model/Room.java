package kaukau.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Point;

public class Room {
	
	private Tile[][] board;
	private HashMap<Point, PickupableItem> items = new HashMap<Point, PickupableItem>();
	private String name;
	private int width, height;
	private int keyCode = 0;
	
	public enum TileType{
		WALL, EMPTY, DOOR;
	}
	
	public Room(String name, String filename) throws IOException{
		this.name = name;
		createRoomFromFile(filename);
	}

	private void createRoomFromFile(String filename) throws IOException {
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
		this.width = width;
		this.height = lines.size();
		board = new Tile[width][lines.size()];
		for(int y=0;y!=lines.size();++y) {
			line = lines.get(y);
			for(int x=0;x!=width;++x) {
				char c = line.charAt(x);
				switch (c) {
					case 'W' :
						addWall(x, y);
						break;
					case 'D':
						addDoor(x, y);
						break;
					case 'C':
						addEmptyTile(x, y);
						break;
					case 'N':
						addItem(x,y);
						break;					
				}
		}
		
	     }

	}

	private void addDoor(int x, int y) {
		board[x][y] = new Tile(TileType.DOOR, x, y);
		
	}

	private void addWall(int x, int y) {
		board[x][y] = new Tile(TileType.WALL, x, y);
	}
	
	private void addEmptyTile(int x, int y) {
		board[x][y] = new Tile(TileType.EMPTY, x, y);
	}
	
	private void addItem(int x, int y) {
		board[x][y] = new Tile(TileType.EMPTY, x, y);
		Key key = new Key(++keyCode, "Key 1");
		Point p = new Point(x, y);
		items.put(p, key);
	}
	
	public Tile getTileAt(Point p){
		return board[p.x][p.y];
	}
	
	public int width(){ return width; }
	public int height(){ return height; }
}
