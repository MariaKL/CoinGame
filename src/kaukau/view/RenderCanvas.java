package kaukau.view;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import kaukau.model.GameMap;
import kaukau.model.GameWorld;
import kaukau.model.Item;
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
	
	// stores a array representation of the current 
	// game level that is to be rendered
	private Block[][] levelBlocks;
	
	/**This class take a gameworld parameter and 
	 * creates a rendering based on the state of 
	 * the game.
	 * @param game
	 */
	public RenderCanvas(GameWorld game){
		
		levelBlocks = new Block[10][10];
		initBlocks(game);
		attachBindings();
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
		for(int r=0; r!=levelBlocks.length; r++){
			for(int c=0; c!=levelBlocks[0].length; c++){
				// getting the gameworld model tile
				Tile tile = tiles[c][r];
				// the block to be created
				Block b = null;
				// Creating a Tile block for rendering
				if(tile.getTileType() == GameMap.TileType.TILE){
					// getting the x & y position of the tile
					int x = r * tileWidth;
					int y = c * tileHeight;
					// converting 2d point to isometic
					Point pos = RenderWindow.twoDToIso(new Point(x, y));		
					// adjusting the position of the render
					b = new RenderTile(0, pos.x+50, pos.y+65);
					// setting the tile item if one
					if(tile.getItem()!=null){
						((RenderTile)b).setItem(tile.getItem());
					}
					allTiles.add((RenderTile) b);
					// adding blocks in order for painters algorithm
					blocks.add(b);
					levelBlocks[c][r] = b;
				// creating a cracked tile block for rendering
				} else if (tile.getTileType() == GameMap.TileType.TILE_CRACKED){
					// getting the x & y position of the tile
					int x = r * tileWidth;
					int y = c * tileHeight;
					// converting 2d point to isometic
					Point pos = RenderWindow.twoDToIso(new Point(x, y));		
					// adjusting the position of the render
					b = new RenderTile(1, pos.x+50, pos.y+65);
					// setting the tile item if one
					if(tile.getItem()!=null){
						((RenderTile)b).setItem(tile.getItem());
					}
					allTiles.add((RenderTile) b);
					// adding blocks in order for painters algorithm
					blocks.add(b);
					levelBlocks[c][r] = b;
				// Creating a Wall block for rendering 
				} else if(tile.getTileType() == GameMap.TileType.WALL){
					// getting the x & y position of the tile
					int x = r * tileWidth;
					int y = c * tileHeight;
					// converting 2d point to isometic
					Point pos = RenderWindow.twoDToIso(new Point(x, y));
					// setting the wall direction in the level data
					if(r==levelBlocks.length-1){
						// west wall
						b = new Wall('W', pos.x+50, pos.y-170);
					} else if(c==levelBlocks.length-1){
						// south wall
						b = new Wall('S', pos.x+50, pos.y-170);
					} else if(r==0){
						// east wall
						b = new Wall('E', pos.x+50, pos.y-170);
					} else {
						// north wall
						b = new Wall('N', pos.x+50, pos.y-170);
					}
					allWalls.add((Wall) b);
					// adding blocks in order for painters algorithm
					blocks.add(b);
					levelBlocks[c][r] = b;
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
		    	// the level block is a wall tile
		    	if(b instanceof Wall){
		    		Wall w = ((Wall)b);
		    		// only render the north & east wall
		    		if(w.getWallType() == 'N' || w.getWallType() == 'E'){
		    			image = ImageIO.read(new File(IMAGE_PATH + "grey-wall.png"));
		    		// west & south walls are not rendered
		    		} else { continue; }
		    	// the level block is a floor tile
		    	} else {
		    		int type = ((RenderTile) b).getTileType();
		    		switch(type){
		    			case 0:
		    				image = ImageIO.read(new File(IMAGE_PATH + "blue-tile.png"));
		    				break;
		    			case 1:
		    				image = ImageIO.read(new File(IMAGE_PATH + "crack-tile.png"));
		    				break;
		    		}
		    	}	
		    	// drawing the block image to the screen
		    	if(image != null){
		    		g.drawImage(image, b.X() + TILE_MARGIN, b.Y() + (TILE_MARGIN/8), this);
		    	}
		    	// checking if block was a tile & contains an item
		    	if(b instanceof RenderTile){
		    		// getting the item contained in the tile
		    		Item token = ((RenderTile)b).getItem();
			    	if(token != null){
			    		BufferedImage itemImg = null;
			    		// getting the item image
			    		switch(token.getName()){
			    			case "Coin":
			    				itemImg = ImageIO.read(new File(IMAGE_PATH + "coin.png"));
			    				break;	 
			    			case "Key":
			    				itemImg = ImageIO.read(new File(IMAGE_PATH + "cube2.png"));
								break;
			    		}
			    		// draw the item image
			    		if(itemImg != null){
				    		g.drawImage(itemImg, b.X() + TILE_MARGIN, b.Y() + (TILE_MARGIN/8), this);
				    	}
			    	}
		    	}
		    }
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * assign key to actions using key bindings
	 */
	private void attachBindings() {
		//rotate world binding
		this.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_R, 0), "rotate");
		this.getActionMap().put("rotate", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				rotateWorld();
				repaint();
			}
		});
	}

	/**Rotates the current game level by applying
	 *  a 90 degree rotation on the current level
	 *  data.
	 */
	protected void rotateWorld() {
		//rotate int[][] 90 degrees into new 2d array
		final int M = levelBlocks.length;
	    final int N = levelBlocks[0].length;
	    
	    // rotate the array data
	    Block[][] ret = new Block[M][N];
	    for (int r = 0; r < M; r++) {
	        for (int c = 0; c < N; c++) {
	    	    // rotating the level 90 degree
	            ret[c][M-1-r] = levelBlocks[r][c];
	        }
	    }
	    //clear old lists, insert new rotated tile arrangement
	    allTiles.clear();
	    allWalls.clear();
	    blocks.clear();
	    for (int i = 0; i<ret.length; i++){
			for (int j = 0; j<ret[0].length; j++){
				// getting the x & y position of the tile
				int x = j * tileWidth;
				int y = i * tileHeight;
				Block b = ret[i][j];
				// block is a wall
				if(b instanceof Wall){
					// converting 2d point to isometic
					Point pos = RenderWindow.twoDToIso(new Point(x, y));
					// getting the new direction of the wall
					if(i==ret.length-1){
						// west wall
						b = new Wall('W', pos.x+50, pos.y-170);
					} else if(j==ret.length-1){
						// south wall
						b = new Wall('S', pos.x+50, pos.y-170);
					} else if(i==0){
						// east wall
						b = new Wall('E', pos.x+50, pos.y-170);
					} else {
						// north wall
						b = new Wall('N', pos.x+50, pos.y-170);
					}
					// adding wall to a list of all walls
					allWalls.add((Wall) b);
					// adding blocks in order for painters algorithm
					blocks.add(b);
				// block is a tile
				} else {
					// converting 2d point to isometic
					Point pos = RenderWindow.twoDToIso(new Point(x, y));
					// getting the tiles item if there is one
					Item item = ((RenderTile)b).getItem();
					// getting the tile type
					int type = ((RenderTile)b).getTileType();
					switch(type){
						case 0:
							// blue tile
							b = new RenderTile(0, pos.x+50, pos.y+65);
							break;
						case 1:
							// cracked tile
							b = new RenderTile(1, pos.x+50, pos.y+65);
							break;
					}
					// setting the tiles item if there is one
					((RenderTile)b).setItem(item);
					// adding tile to a list of all tiles
					allTiles.add((RenderTile) b);
					// adding blocks in order for painters algorithm
					blocks.add(b);
				}			
			}
	    }
	    //replace levelBlocks array with new 2d array for future rotations
	    for(int a=0; a<levelBlocks.length; a++)
	    	  for(int b=0; b<levelBlocks[0].length; b++)
	    	    levelBlocks[a][b]=ret[a][b];
	}
}
