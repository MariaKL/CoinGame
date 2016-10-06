package kaukau.view;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import kaukau.model.GameMap;
import kaukau.model.GameWorld;
import kaukau.model.Tile;

@SuppressWarnings("serial")
public class RenderCanvas extends JPanel {
	
	// path to the images folder
	private static final String IMAGE_PATH = "images/";
	
	// Field to store all the tiles in the current level
	private List<RenderTile> allTiles = new ArrayList<RenderTile>();
	private List<Wall> allWalls = new ArrayList<Wall>();

	private final int tileWidth = 50;
	private final int tileHeight = 50;
	
	public RenderCanvas(GameWorld game){
		
		getBlocks(game);
		repaint();

	}

	private void getBlocks(GameWorld game) {

		Tile[][] tiles = game.getGameTiles();
		for(int r=0; r!=7; r++){
			for(int c=0; c!=7; c++){
				// getting the gameworld model tile
				Tile tile = tiles[r][c];
				if(tile.getTileType() == GameMap.TileType.EMPTY){
					// getting the x & y position of the tile
					int x = r * tileWidth ;
					int y = c * tileHeight;
					// converting 2d point to isometic
					Point pos = RenderWindow.twoDToIso(new Point(x, y));
					// adjusting the position of the render
					allTiles.add(new RenderTile(0, pos.x-25, pos.y+130));
				} else if(tile.getTileType() == GameMap.TileType.WALL){
					// getting the x & y position of the tile
					int x = r * tileWidth ;
					int y = c * tileHeight;
					// converting 2d point to isometic
					Point pos = RenderWindow.twoDToIso(new Point(x, y));
					// adjusting the position of the render
					allWalls.add(new Wall(0, pos.x-25, pos.y-105));
				}
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		paintLevelWalls(g);
		paintLevelTiles(g);
		
	}
	
	private void paintLevelWalls(Graphics g) {
		final int TILE_MARGIN = 400;
		try{
	    	BufferedImage image = null;
	    	Boolean pattern = true;
		    for(Wall w: allWalls){
		    	if(pattern){
		    		image = ImageIO.read(new File(IMAGE_PATH + "light-wall.png"));
		    		pattern = false;
		    	} else {
		    		image = ImageIO.read(new File(IMAGE_PATH + "grey-wall.png"));
		    		pattern = true;
		    	}	
		    	if(image != null){
		    		g.drawImage(image, w.X() + TILE_MARGIN, w.Y() + (TILE_MARGIN/8), this);
		    	}
		    }
		} catch(IOException e){
			e.printStackTrace();
		}
		
	}

	private void paintLevelTiles(Graphics g) {
		final int TILE_MARGIN = 400;
		try{
	    	BufferedImage image = null;
	    	int count = 0;
		    for(RenderTile t: allTiles){
		    	if(count%5==0){
		    		image = ImageIO.read(new File(IMAGE_PATH + "crack-tile.png"));
		    	} else if(count%8==0){
		    		image = ImageIO.read(new File(IMAGE_PATH + "red-tile.png"));
		    	} else {
		    		image = ImageIO.read(new File(IMAGE_PATH + "blue-tile.png"));
		    	} 
		    	if(image != null){
		    		g.drawImage(image, t.X() + TILE_MARGIN, t.Y() + (TILE_MARGIN/8), this);
		    	}
		    	count++;
		    }
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}
