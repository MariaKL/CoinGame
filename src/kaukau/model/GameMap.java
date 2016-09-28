package kaukau.model;

import java.awt.Point;

public class GameMap {

	private static final int ROOM_WIDTH = 7;
	private static final int ROOM_HEIGHT = 7;
	private static final int WIDTH = ROOM_WIDTH * 2;
	private static final int HEIGHT = ROOM_HEIGHT * 2;
	
	private Room[][] rooms = new Room[2][2];
	
	public GameMap(){
		this.rooms = new Room[ROOM_WIDTH][ROOM_WIDTH];
	}
	
	public int width(){ return ROOM_WIDTH * rooms.length; }
	public int height(){ return ROOM_HEIGHT * rooms[0].length; }
	
	public Room getRoom(Point p){
		if (p.x >= WIDTH || p.y > HEIGHT) return null;
		return rooms[p.x][p.y];
	}
}
