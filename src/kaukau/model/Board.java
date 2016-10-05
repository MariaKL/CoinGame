package kaukau.model;

import java.awt.Point;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import kaukau.storage.ReadXMLFile;

public class Board {

	private static final int BOARD_WIDTH = 14;
	private static final int BOARD_HEIGHT = 14;
	private Tile[][] board;
	private ArrayList<Room> rooms;

	public enum TileType{
		WALL, EMPTY, DOOR;
	}

	public GameMap(String fileName){
		this.board= new Tile[BOARD_WIDTH][BOARD_HEIGHT];
		this.rooms = new ArrayList<Room>();
		createRoomsFromFile();

	}

	public void createRoomsFromFile(){
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
	}

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

	public int width(){ return board.length; }
	public int height(){ return board[0].length; }
}
