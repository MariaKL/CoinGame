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
	public Point getStartPoint(){
		return new Point(startX, startY);
	}
	public String getName(){ return name; }
}
