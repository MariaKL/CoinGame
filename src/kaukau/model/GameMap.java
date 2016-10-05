package kaukau.model;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import kaukau.storage.ReadXMLFile;

public class GameMap implements Serializable{
	private static final int ROOM_WIDTH = 7;
	private static final int ROOM_HEIGHT = 7;
	private static final int BOARD_WIDTH = 14;
	private static final int BOARD_HEIGHT = 14;
	private Tile[][] board;
	private ArrayList<Room> rooms;
	private ArrayList<Door> doors;

	public enum TileType{
		WALL, EMPTY, DOOR;
	}


	public GameMap(){
		this.board= new Tile[BOARD_WIDTH][BOARD_HEIGHT];
		this.rooms = new ArrayList<Room>();
		this.doors = new ArrayList<Door>();
		createRoomsFromFile();
	}

	public void createRoomsFromFile(){
		try {
			Document doc = new ReadXMLFile().createDocument("Rooms.xml");
			NodeList nList = doc.getElementsByTagName("room");  // get elements

    		for (int temp = 0; temp < nList.getLength(); temp++) {
    			Node nNode = nList.item(temp);
    			//System.out.println("\nCurrent Element :" + nNode.getNodeName());

    			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
    				Element eElement = (Element) nNode;
    				int doorCount = 0;  // use to count the doors
    				int keyCount = 0;   // use to count the keys
    				// get the name of the room, the start point of x and y
    				String name = eElement.getAttribute("name");
    				int startX =  Integer.valueOf(eElement.getElementsByTagName("startX").item(0).getTextContent());
    				int startY =  Integer.valueOf(eElement.getElementsByTagName("startY").item(0).getTextContent());
    				Room room = new Room(name, startX, startY);
    				rooms.add(room);

    				for (int row = 0; row < ROOM_HEIGHT; row++){
    					// get the value line by line from XML file e.g. a line is <L0></L0>
	    				String line = eElement.getElementsByTagName("L"+String.valueOf(row)).item(0).getTextContent();
	    				for (int col = 0; col < ROOM_WIDTH; ++col) {
	    					char c = line.charAt(col);
	    					switch (c) {
		    					case 'W' :
		    						board[col+startX][row+startY] = new Tile(TileType.WALL, col+startX, row+startY);
		    						break;
		    					case 'D':
		    						doorCount = addDoor(eElement, doorCount, col+startX, row+startY);
		    						break;
		    					case 'K':
		    						keyCount = addKey(eElement, keyCount, col+startX, row+startY);
		    						break;
		    					case 'C':
		    						board[col+startX][row+startY] = new Tile(TileType.EMPTY, col+startX, row+startY);
		    						break;
	    					}
	    				}

	    			}
    			}

    		}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add the door to the board.
	 * @param element room element from XML file
	 * @param count the number index of the door from XML file
	 * @param x the x point of this door
	 * @param y the y point of this door
	 * @return the next index number for next door item
	 */
	public int addDoor(Element element, int count, int x, int y){
		Tile tile = new Tile(TileType.DOOR, x, y);
		int key = Integer.valueOf(element.getElementsByTagName("D"+String.valueOf(count)).item(0).getTextContent());
		Door door = new Door(key, tile);
		doors.add(door);
		tile.setItem(door);
		board[x][y] = tile;
		return ++count;
	}

	/**
	 *
	 * @param element room element from XML file
	 * @param count the number index of the key from XML file
	 * @param x the x point of this key
	 * @param y the y point of  this key
	 * @return the next index number for next key item
	 */
	public int addKey(Element element, int count, int x, int y){
		Tile tile = new Tile(TileType.EMPTY, x, y);
		int keycode = Integer.valueOf(element.getElementsByTagName("K"+String.valueOf(count)).item(0).getTextContent());
		Key key = new Key(keycode);
		tile.setItem(key);
		System.out.println(tile.containsPickupItem());
		board[x][y] = tile;
		return ++count;
	}

	/*public void createRoomsFromFile(){
		try {
			Document doc = new ReadXMLFile().createDocument("room.xml");
			NodeList nList = doc.getElementsByTagName("room");  // get elements

    		for (int temp = 0; temp < nList.getLength(); temp++) {
    			Node nNode = nList.item(temp);
    			//System.out.println("\nCurrent Element :" + nNode.getNodeName());

    			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
    				Element eElement = (Element) nNode;
    				String name = eElement.getAttribute("name");

    				for (int x = 0; x < 14; x++){
    					// get the value line by line from XML file e.g. a line is <L0></L0>
	    				String line = eElement.getElementsByTagName("L"+String.valueOf(x)).item(0).getTextContent();
	    				for (int y = 0; y < 14; y++) {

	    					char c = line.charAt(y);
	    					switch (c) {
		    					case 'W' :
		    						board[x][y] = new Tile(TileType.WALL, x, y);
		    						break;
		    					case 'D':
		    						board[x][y] = new Tile(TileType.DOOR, x, y);
		    						break;
		    					case 'C':
		    						board[x][y] = new Tile(TileType.EMPTY, x, y);
		    						break;
	    					}
	    				}

	    			}
    			}

    		}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/**
	 * Return the tile at the given point.
	 * @param p the point of a tile
	 * @return a tile of the board
	 */
	public Tile getTileAt(Point p){
		return board[p.x][p.y];
	}

	/**
	 * Return the current board in tiles.
	 * @return the current board
	 */
	public Tile[][] getBoard(){
		return board;
	}

	public ArrayList<Room> getAllRooms(){
		return rooms;
	}

	/**
	 * Return the width of this board.
	 * @return the width of board
	 */
	public int width(){ return BOARD_WIDTH; }

	/**
	 * Return the height of this board.
	 * @return the height of board
	 */
	public int height(){ return BOARD_HEIGHT; }
}
