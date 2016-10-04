package kaukau.model;

import java.awt.Point;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import kaukau.model.Room.TileType;
import kaukau.storage.ReadXMLFile;

public class GameMap {
	
	private static final int ROOM_WIDTH = 7;
	private static final int ROOM_HEIGHT = 7;
	private Tile[][] board;
	
	public GameMap(String fileName){
		this.board= new Tile[15][15];;
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
    				int startX = Integer.parseInt(eElement.getElementsByTagName("StartX").item(0).getTextContent());
    				int startY = Integer.parseInt(eElement.getElementsByTagName("StartY").item(0).getTextContent());
    				
    				for (int x = startX; x < ROOM_HEIGHT; x++){
    					// get the value line by line from XML file e.g. a line is <L0></L0>
	    				String line = eElement.getElementsByTagName("L"+String.valueOf(x)).item(0).getTextContent();
	    				for(int y = startY;y!= ROOM_WIDTH;++y) {
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
	
	public int width(){ return board.length; }
	public int height(){ return board[0].length; }
	
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
}
