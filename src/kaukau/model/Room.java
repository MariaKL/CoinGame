package kaukau.model;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Room implements Serializable{

	@XmlElement(name="name")
	private String name;
	@XmlElement(name="startX")
	private int startX;
	@XmlElement(name="startY")
	private int startY;
	private ArrayList<Door> doors;

	public Room(String name, int x, int y){
		this.name = name;
		this.startX = x;
		this.startY = y;
		doors = new ArrayList<Door>();
	}
	
	public Room(){
		this(null, -1, -1);
	}

	public Point getStartPoint(){
		return new Point(startX, startY);
	}
	
	public void addDoor(Door door){
		this.doors.add(door);
	}
	
<<<<<<< HEAD
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
=======
*/
	
	public Point getStartPoint(){
		return new Point(startX, startY);
	}
	
	
	public String getName(){ return name; }
>>>>>>> 008f95d8520f6c81771f9109b7aa079f5c4a1371
}
