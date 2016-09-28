package kaukau.model;

import java.awt.Point;

public class GameMap {

	private Room[][] rooms;
	private int width, height;
	
	public GameMap(int width, int height){
		this.width = width;
		this.height = height;
		this.rooms = new Room[width][height];
	}
	
	public int width(){ 
		int totalWidth = 0; 
		for (int i = 0; i < rooms[0].length; i++){
			totalWidth += rooms[0][i].width();
		}
		return totalWidth; 
	}
	public int height(){ return height; }
	
	public Room getRoom(Point p){
		if (p.x > width || p.y > height) return null;
		return rooms[p.x][p.y];
	}
}
