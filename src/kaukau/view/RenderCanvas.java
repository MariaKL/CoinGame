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
	private char[][] levelData;
	
	/**This class take a gameworld parameter and 
	 * creates a rendering based on the state of 
	 * the game.
	 * @param game
	 */
	public RenderCanvas(GameWorld game){
		
		levelData = new char[10][10];
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
		for(int r=0; r!=levelData.length; r++){
			for(int c=0; c!=levelData[0].length; c++){
				// getting the gameworld model tile
				Tile tile = tiles[c][r];
				// the block to be created
				Block b = null;
				// Creating a Tile block for rendering
				if(tile.getTileType() == GameMap.TileType.EMPTY){
					// setting the tile in the level data
					levelData[c][r] = 'T';
					// getting the x & y position of the tile
					int x = r * tileWidth;
					int y = c * tileHeight;
					// converting 2d point to isometic
					Point pos = RenderWindow.twoDToIso(new Point(x, y));
					// adjusting the position of the render
					b = new RenderTile(0, pos.x+50, pos.y+65);
					allTiles.add((RenderTile) b);
					// adding blocks in order for painters algorithm
					blocks.add(b);
				// Creating a Wall block for rendering 
				} else if(tile.getTileType() == GameMap.TileType.WALL){
					// setting the wall direction in the level data
					if(r==levelData.length-1){
						levelData[c][r] = 'W';
					} else if(c==levelData.length-1){
						levelData[c][r] = 'S';
					} else if(r==0){
						levelData[c][r] = 'E';
					} else {
						levelData[c][r] = 'N';
					}
					// getting the x & y position of the tile
					int x = r * tileWidth;
					int y = c * tileHeight;
					// converting 2d point to isometic
					Point pos = RenderWindow.twoDToIso(new Point(x, y));
					// adjusting the position of the render
					b = new Wall(levelData[c][r], pos.x+50, pos.y-170);
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
		    		image = ImageIO.read(new File(IMAGE_PATH + "blue-tile.png"));
		    	}	
		    	// drawing the block image to the screen
		    	if(image != null){
		    		g.drawImage(image, b.X() + TILE_MARGIN, b.Y() + (TILE_MARGIN/8), this);
		    	}
		    }
		} catch(IOException e){
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
		final int M = levelData.length;
	    final int N = levelData[0].length;
	    
	    // rotate the array data
	    char[][] ret = new char[M][N];
	    for (int r = 0; r < M; r++) {
	        for (int c = 0; c < N; c++) {
	    	    // switching the direction of blocks
	    	    char dir = levelData[r][c];
	    	    switch(dir){
	    	    	case 'T':
	    	    		break;
	    	    	case 'N':
	    	    		levelData[r][c] = 'E';
	    	    		break;
	    	    	case 'E':
	    	    		levelData[r][c] = 'S';
	    	    		break;
	    	    	case 'S':
	    	    		levelData[r][c] = 'W';
	    	    		break;
	    	    	case 'W':
	    	    		levelData[r][c] = 'N';
	    	    		break;
	    	    }
	            ret[c][M-1-r] = levelData[r][c];
	        }
	    }
	    //clear old list, insert new tile arrangement
	    allTiles.clear();
	    allWalls.clear();
	    blocks.clear();
	    for (int i = 0; i<ret.length; i++){
			for (int j = 0; j<ret[0].length; j++){
				// getting the x & y position of the tile
				int x = j * tileWidth;
				int y = i * tileHeight;
				// getting the tile type
				char tileType = ret[i][j];
				Block b = null;
				// block is a tile
				if(tileType != 'T'){
					// setting the wall direction in the level data
					if(i==ret.length-1){
						ret[i][j] = 'W';
					} else if(j==ret.length-1){
						ret[i][j] = 'S';
					} else if(i==0){
						ret[i][j] = 'E';
					} else {
						ret[i][j] = 'N';
					}
					// converting 2d point to isometic
					Point pos = RenderWindow.twoDToIso(new Point(x, y));
					// adjusting the position of the render
					b = new Wall(ret[i][j], pos.x+50, pos.y-170);
					allWalls.add((Wall) b);
					// adding blocks in order for painters algorithm
					blocks.add(b);
				} else {
					// converting 2d point to isometic
					Point pos = RenderWindow.twoDToIso(new Point(x, y));
					// adjusting the position of the render
					b = new RenderTile(0, pos.x+50, pos.y+65);
					allTiles.add((RenderTile) b);
					// adding blocks in order for painters algorithm
					blocks.add(b);
				}			
			}
	    }
	    //replace levelData with new 2d array for future rotations
	    for(int a=0; a<levelData.length; a++)
	    	  for(int b=0; b<levelData[0].length; b++)
	    	    levelData[a][b]=ret[a][b];
	}
}