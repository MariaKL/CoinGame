package kaukau.model;


import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.awt.Point;
import java.io.Serializable;

public class Room implements Serializable{

	private static final int ROOM_WIDTH = 7;
	private static final int ROOM_HEIGHT = 7;
	private Tile[][] board = new Tile[ROOM_WIDTH][ROOM_HEIGHT];
	private String name;
	private int keyCode = 0;
	private int startX, startY;

	public Room(String name, int x, int y){
		this.name = name;
		startX = x;
		startY = y;
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

	public String getName(){ return name; }
}
