package kaukau.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**This class handles the rendering of the game levels
 * 	in the Kaukau game. This class interacts with the
 * 	application window to form the gui of the game.	
 * 
 * @author Patrick
 *
 */
public class RenderWindow extends JPanel {
	
	// path to the images folder
	private static final String IMAGE_PATH = "images/";
	
	/* Field to store 2D array representation of the game level data.	
	 * 	KEY:
	 * 	1 - horizontal wall tile
	 * 	2 - vertical wall tile
	 * 	3 - top left corner tile
	 * 	4 - top right corner tile
	 * 	5 - bottom right corner tile
	 * 	6 - bottom left corner tile
	 */
	private static final int[][] levelData = {{0,2,0,0,0,2,9},
		                                     {2,2,0,0,0,2,9},
		                                     {2,0,0,0,2,0,9},
		                                     {0,0,0,0,0,0,9},
		                                     {0,2,2,0,2,2,9},
		                                     {2,0,0,2,2,2,9},
		                                     {0,2,0,0,2,0,9}}; 
	
	// Field to store the board margin in pixels
	private static final int MARGIN = 325;
	
	// Fields to store the tile width & height in pixels
	private static final int tileWidth = 50;
	private static final int tileHeight = 50;
	
	// Field to store all the tiles in the current level
	List<Tile> allTiles = new ArrayList<Tile>();
	
	public RenderWindow(){
		//Setting a border
		// FIXME: Stop border resizing with window
		setBorder(BorderFactory.createCompoundBorder(
						      BorderFactory.createEmptyBorder(0, 0, 32, 0),
						      BorderFactory.createLineBorder(Color.BLACK, 2)
						   ));
		
		// initilising the level from 2D level data array
		initLevel();
		repaint();
	}

	/**
	 * Initilising the game level from the 2D level data
	 * 	array.
	 */
	private void initLevel() {
		for (int i = 0; i<levelData.length; i++){
			for (int j = 0; j<levelData[0].length; j++){
				
				// getting the x & y position of the tile
				int x = j * tileWidth;
				int y = i * tileHeight;
				
				// getting the type type
				int tileType = levelData[i][j];
				
				// creating and adding the tile to the current level board
				placeTile(tileType, twoDToIso(new Point(x, y)));
			}
		}	
	}
	
	/**This method creates and adds a tile to a list
	 *  of all tiles which make up the current game level
	 * 
	 * @param tileType
	 * @param tile position point
	 */
	private void placeTile(int tileType, Point pt) {
		// Creating a new tiles
		Tile t = new Tile(tileType, pt.x, pt.y);
		// Adding the tile to the current level
		allTiles.add(t);
		
	}

	@Override
	 public void paintComponent(Graphics g) {
	     super.paintComponent(g);
	     
	     try {
	    	 
	    	//FIXME: drawing walls
	    	BufferedImage wallImg = ImageIO.read(new File(IMAGE_PATH + "north-wall.png"));
	    	int x = 284;
	    	int y = -66;
	    	for(int i = 0; i != 8; i++){
	    		g.drawImage(wallImg, x, y, this);
	    		x = x - 42;
	    		y = y + 22;
	    	}
			
			// FIXME: tile randomisation
	    	// Drawing all level tiles onto the rendering panel
	    	BufferedImage image;
		    for(Tile t: allTiles){
		    	if(t.getTileType()==9){
		    		image = null;
		    	} else if(t.getTileType() == 0){
		    		image = ImageIO.read(new File(IMAGE_PATH + "blue-tile.png"));
		    	} else {
		    		image = ImageIO.read(new File(IMAGE_PATH + "crack-tile.png"));
		    	}
		    	if(image != null){
		    		g.drawImage(image, t.X() + MARGIN, t.Y() + (MARGIN/4), this);
		    	}
		    }
		    
		    // FIXME: drawing character sprite to board.
		    image = ImageIO.read(new File(IMAGE_PATH + "man-se-64.png"));
    		g.drawImage(image, 342, -6, this);
    		
		} catch (IOException e) {
			e.printStackTrace();
		}   
	 }
	
	/*
	 * HELPER METHODS
	 */
	/**
     * convert a 2d point to isometric
     */
	private static Point twoDToIso (Point pt){
		Point result = new Point(0,0);
		result.x = pt.x - pt.y;
		result.y = (pt.x + pt.y) / 2;
		return(result);
	}
	/**
     * convert an isometric point to 2D
     * */
	private static Point isoTo2D(Point pt) {
		Point result = new Point(0, 0);
		result.x = (2 * pt.y + pt.x) / 2;
		result.y = (2 * pt.y - pt.x) / 2;
		return(result);
	}
    /**
     * convert a 2d point to specific tile row/column
     * */
    public static Point getTileCoordinates(Point pt, int tileHeight) {
        Point result = new Point(0,0);
        result.x=(int) Math.floor(pt.x/tileHeight);
        result.y=(int) Math.floor(pt.y/tileHeight);
        return(result);
    }
    /**
     * convert specific tile row/column to 2d point
     * */
    public static Point get2dFromTileCoordinates(Point pt, int tileHeight) {
        Point result = new Point(0,0);
        result.x=pt.x*tileHeight;
        result.y=pt.y*tileHeight;
        return(result);
    }
}
