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
	
	// stores the width and height constants of the board tiles
	private final int tileWidth = 50;
	private final int tileHeight = 50;
	
	// Field to store all the walls & tiles in the current level
	private List<RenderTile> allTiles = new ArrayList<RenderTile>();
	private List<Wall> allWalls = new ArrayList<Wall>();
	
	// Field for the blocks to be rendered for the current level
	private List<Block> blocks = new ArrayList<Block>();
	
	/**This class take a gameworld parameter and 
	 * creates a rendering based on the state of 
	 * the game.
	 * @param game
	 */
	public RenderCanvas(GameWorld game){
		
		initBlocks(game);
		repaint();

	}

	/**initialises the blocks (tiles & walls) which make up
	 * the current level. The tiles are recieved from the 
	 * passed GameWorld parameter.
	 * 
	 * @param game
	 */
	private void initBlocks(GameWorld game) {

		Tile[][] tiles = game.getGameTiles();
		for(int r=0; r!=7; r++){
			for(int c=0; c!=7; c++){
				// getting the gameworld model tile
				Tile tile = tiles[r][c];
				// the block to be created
				Block b = null;
				// Creating a Tile block for rendering
				if(tile.getTileType() == GameMap.TileType.EMPTY){
					// getting the x & y position of the tile
					int x = r * tileWidth ;
					int y = c * tileHeight;
					// converting 2d point to isometic
					Point pos = RenderWindow.twoDToIso(new Point(x, y));
					// adjusting the position of the render
					b = new RenderTile(0, pos.x, pos.y+65);
					allTiles.add((RenderTile) b);
					// adding blocks in order for painters algorithm
					blocks.add(b);
				// Creating a Wall block for rendering 
				} else if(tile.getTileType() == GameMap.TileType.WALL){
					// getting the x & y position of the tile
					int x = r * tileWidth ;
					int y = c * tileHeight;
					// converting 2d point to isometic
					Point pos = RenderWindow.twoDToIso(new Point(x, y));
					// adjusting the position of the render
					b = new Wall(0, pos.x, pos.y-170);
					allWalls.add((Wall) b);
					// adding blocks in order for painters algorithm
					blocks.add(b);
				}
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		paintBlocks(g);
		
		//paintLevelWalls(g);
		//paintLevelTiles(g);
		
	}
	
	/**Paints the blocks which make up the current level.
	 *  the blocks are added to the list from front to back
	 *  and thus can be correctly rendered in order. 
	 */
	private void paintBlocks(Graphics g) {
		final int TILE_MARGIN = 400;
		try{
	    	BufferedImage image = null;
		    for(Block b: blocks){
		    	if(b instanceof Wall){
		    		image = ImageIO.read(new File(IMAGE_PATH + "grey-wall.png"));
		    	} else {
		    		image = ImageIO.read(new File(IMAGE_PATH + "blue-tile.png"));
		    	}	
		    	if(image != null){
		    		g.drawImage(image, b.X() + TILE_MARGIN, b.Y() + (TILE_MARGIN/8), this);
		    	}
		    }
		} catch(IOException e){
			e.printStackTrace();
		}
		
	}

	/*
	private void paintLevelWalls(Graphics g) {
		final int TILE_MARGIN = 400;
		try{
	    	BufferedImage image = null;
	    	Boolean pattern = true;
		    for(Wall w: allWalls){
		    	if(pattern){
		    		image = ImageIO.read(new File(IMAGE_PATH + "grey-wall.png"));
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
	*/
}
