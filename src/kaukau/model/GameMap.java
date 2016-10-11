package kaukau.model;

import java.awt.Point;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import kaukau.model.GameMap.TileType;

@XmlRootElement
@XmlType(propOrder = {"allDoors", "boardTiles"})
public class GameMap implements Serializable {

	public static final int ROOM_WIDTH = 10;
	public static final int ROOM_HEIGHT = 10;
	public static final int BOARD_WIDTH = 20;
	public static final int BOARD_HEIGHT = 20;

	private Tile[][] board;
	private ArrayList<Room> rooms;
	private ArrayList<Door> doors;

	public enum TileType{
		WALL, DOOR, TILE, TILE_CRACKED;
	}

	public GameMap() {
		this.board = new Tile[BOARD_WIDTH][BOARD_HEIGHT];
		this.rooms = new ArrayList<Room>();
		this.doors = new ArrayList<Door>();
		createRoomsFromFile();
	}

	public void createRoomsFromFile() {
		try {
			// Document doc = new ReadXMLFile().createDocument("Rooms.xml");
			File xmlFile = new File("Rooms.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xmlFile);
			NodeList nList = doc.getElementsByTagName("room");  // get elements

			for (int temp = 0; temp < nList.getLength(); temp++) {
    			Node nNode = nList.item(temp);
    			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
    				Element eElement = (Element) nNode;
    				int doorCount = 0;  // use to count the doors
    				int keyCount = 0;   // use to count the keys
    				int coinCount = 0;   // use to count the coin
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
	    					int x = row+startX;
	    					int y = col+startY;
	    					switch (c) {
		    					case 'W' :
		    						board[x][y] = new Tile(TileType.WALL, x, y);
		    						break;
		    					case 'D':
		    						doorCount = addDoor(eElement, doorCount, x, y, room);
		    						break;
		    					case 'K':
		    						keyCount = addKey(eElement, keyCount, x, y);
		    						break;
		    					case 'X':
		    						keyCount = addCoin(eElement, coinCount, x, y);
		    						break;
		    					case 'T':
		    						board[x][y] = new Tile(TileType.TILE, x, y);
		    						break;
		    					case 'C':
		    						board[x][y] = new Tile(TileType.TILE_CRACKED, x, y);
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
	 *
	 * @param element room element from XML file
	 * @param count the number index of the door from XML file
	 * @param x the x point of this door
	 * @param y the y point of this door
	 * @return the next index number for next door item
	 */
	public int addDoor(Element element, int count, int x, int y, Room room){
		Tile tile = new Tile(TileType.DOOR, x, y);
		int key = Integer.valueOf(element.getElementsByTagName("Door" + String.valueOf(count)).item(0).getTextContent());
		Door door = new Door(key);
		doors.add(door);
		tile.setItem(door);
		room.addDoor(door);
		board[x][y] = tile;
		return ++count;
	}

	/**
	 *
	 * @param element
	 *            room element from XML file
	 * @param count
	 *            the number index of the key from XML file
	 * @param x
	 *            the x point of this key
	 * @param y
	 *            the y point of this key
	 * @return the next index number for next key item
	 */
	public int addKey(Element element, int count, int x, int y){
		Tile tile = new Tile(TileType.TILE, x, y);
		int keycode = Integer.valueOf(element.getElementsByTagName("Key"+String.valueOf(count)).item(0).getTextContent());
		Key key = new Key(keycode);
		tile.setItem(key);
		board[x][y] = tile;
		return ++count;
	}

	/**
	 *
	 * @param element
	 *            room element from XML file
	 * @param count
	 *            the number index of the key from XML file
	 * @param x
	 *            the x point of this key
	 * @param y
	 *            the y point of this key
	 * @return the next index number for next key item
	 */
	public int addCoin(Element element, int count, int x, int y){
		Tile tile = new Tile(TileType.TILE, x, y);
		int amount = Integer.valueOf(element.getElementsByTagName("Coin"+String.valueOf(count)).item(0).getTextContent());
		Coin coin = new Coin(amount);
		tile.setItem(coin);
		board[x][y] = tile;
		return ++count;
	}

	/**
	 * Return the tile at the given point.
	 *
	 * @param p
	 *            the point of a tile
	 * @return a tile of the board
	 */
	public Tile getTileAt(Point p) {
		return board[p.x][p.y];
	}

	/**
	 * Return the current board in tiles.
	 *
	 * @return the current board
	 */
	public Tile[][] getBoard() {
		return board;
	}


	@XmlElement(name = "boardTiles")
	public ArrayList<Row> getBoardTiles() {
		ArrayList<Row> board = new ArrayList<Row>();
		ArrayList<Tile> row;
		for (int i = 0; i < getBoard().length; i++) {
			row = new ArrayList<Tile>();
			for (int j = 0; j < getBoard()[0].length; j++) {
				row.add(getBoard()[i][j]);
			}
			Row r = new Row();
			r.setRow(row);
			board.add(r);
		}
		return board;
	}

	public void setBoardTiles(ArrayList<Row> rows) {
		for (int i = 0; i < getBoard().length; i++) {
			Row row = rows.get(i);
			for (int j = 0; j < getBoard()[0].length; j++) {
				this.board[i][j] = row.getRows().get(j);
			}
		}
	}

//	@XmlRootElement
//	@XmlAccessorType(XmlAccessType.FIELD)
//	 private static class Row {
//		private ArrayList<Tile> row = new ArrayList<Tile>();
//
//		public Row(ArrayList<Tile> row2) {
//			this.row = row2;
//		}
//
//		public Row() {
//			this(null);
//		}
//
//		@XmlElementWrapper(name = "getRows")
//		@XmlElements({ @XmlElement(name = "tile") })
//		public ArrayList<Tile> getRows() {
//			return this.row;
//		}
//	}

	public ArrayList<Room> getAllRooms() {
		return rooms;
	}

	@XmlElementWrapper(name = "getAllDoors")
	@XmlElements({ @XmlElement(name = "Door") })
	public ArrayList<Door> getAllDoors() {
		return doors;
	}

	/**
	 * Return the width of this board.
	 *
	 * @return the width of board
	 */
	public int width() {
		return BOARD_WIDTH;
	}

	/**
	 * Return the height of this board.
	 *
	 * @return the height of board
	 */
	public int height() {
		return BOARD_HEIGHT;
	}
}
