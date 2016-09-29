package kaukau.model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import kaukau.storage.ReadXMLFile;

import java.awt.Point;

public class Room implements Serializable{

	private static final int ROOM_WIDTH = 7;
	private static final int ROOM_HEIGHT = 7;
	private Tile[][] board = new Tile[ROOM_WIDTH][ROOM_HEIGHT];
	private String name;
	private int keyCode = 0;

	public enum TileType{
		WALL, EMPTY, DOOR;
	}

	public Room(String name, String doc) throws IOException{
		this.name = name;
		createRoomFromFile(doc);
	}

	public Room(String doc) throws IOException{
		createRoomFromFile(doc);
	}

	public void createRoomFromFile(String filename){
		try {
			Document doc = new ReadXMLFile().createDocument(filename);
			NodeList nList = doc.getElementsByTagName("room");

    		for (int temp = 0; temp < nList.getLength(); temp++) {
    			Node nNode = nList.item(temp);
    			//System.out.println("\nCurrent Element :" + nNode.getNodeName());

    			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
    				Element eElement = (Element) nNode;
    				this.name = eElement.getAttribute("name");

    				for (int x = 0; x < ROOM_HEIGHT; x++){
    					// get the value line by line from XML file e.g. a line is <L0></L0>
	    				String line = eElement.getElementsByTagName("L"+String.valueOf(x)).item(0).getTextContent();

	    				for(int y = 0;y!= ROOM_WIDTH;++y) {
	    					char c = line.charAt(y);
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

    		}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*private void createRoomFromFile(String filename) throws IOException {
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
		//this.width = width;
		//this.height = lines.size();
		board = new Tile[width][lines.size()];
		for(int y=0;y!=lines.size();++y) {
			line = lines.get(y);
			for(int x=0;x!=width;++x) {
				char c = line.charAt(x);
				System.out.println(c);
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
*/
	private void addDoor(int x, int y) {
		board[x][y] = new Tile(TileType.DOOR, x, y);
	}

	private void addWall(int x, int y) {
		board[x][y] = new Tile(TileType.WALL, x, y);
	}

	private void addEmptyTile(int x, int y) {
		EmptyTile tile = new EmptyTile(TileType.EMPTY, x, y);
		board[x][y] = tile;
	}

	private void addItem(int x, int y) {
		EmptyTile tile = new EmptyTile(TileType.EMPTY, x, y);
		Key key = new Key(++keyCode, "Key 1");
		tile.addItem(key);
		board[x][y] = tile;
		Point p = new Point(x, y);
	}


	public Tile getTileAt(Point p){
		if (p.x >= ROOM_WIDTH || p.y >= ROOM_HEIGHT) return null;
		return board[p.x][p.y];
	}

	// if the tile is not EmptyTile, then it is occupy.
	public boolean isTileOccupied(Point p){
		Tile tile = board[p.x][p.y];
		if (tile.getTileType() == TileType.EMPTY){
			EmptyTile e = (EmptyTile) tile;
			return e.isTileOccupied();
		}
		return true;
	}

	public Tile[][] getRoomBoard(){
		return board;
	}

	public int width(){ return ROOM_WIDTH; }
	public int height(){ return ROOM_HEIGHT; }
	public String getName(){ return name; }
}
