package kaukau.model;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

public class Room implements Serializable{

	private String name;
	private int startX, startY;
	private ArrayList<Door> doors;

	public Room(String name, int x, int y){
		this.name = name;
		this.startX = x;
		this.startY = y;
		doors = new ArrayList<Door>();
	}

	public Point getStartPoint(){
		return new Point(startX, startY);
	}
	
	public void addDoor(Door door){
		this.doors.add(door);
	}
	
	public String getName(){ 
		return name; 
	}
	
	public void setStartX(int x){
		startX = x;
	}
	
	public void setStartY(int y){
		startY = y;
	}
	
	public void setDoors(){
		
	}
	
	public int getStartX(){
		return startX;
	}
	
	public int getStartY(){
		return startY;
	}
	
	public ArrayList<Door> getDoors(){
		return doors;
	}
}
