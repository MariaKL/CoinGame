package kaukau.view;

import java.awt.Container;
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
		
		getTiles(game);
		repaint();

	}

	private void getTiles(GameWorld game) {

		Tile[][] tiles = game.getGameTiles();
		for(int r=0; r!=8; r++){
			for(int c=0; c!=8; c++){
				// getting the gameworld model tile
				Tile tile = tiles[r][c];
				if(tile.getTileType() == GameMap.TileType.EMPTY){
					
					// getting the x & y position of the tile
					int x = r * tileWidth ;
					int y = c * tileHeight;

					// getting the type type
					//int tileType = levelData[i][j];

					// creating and adding the tile to the current level board
					Point pos = RenderWindow.twoDToIso(new Point(x, y));
					// creating & adding the new tile to the all tiles list
					//TODO: different tile types
					allTiles.add(new RenderTile(0, pos.x, pos.y));
				} 
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		paintLevelTiles(g);
	}
	
	private void paintLevelTiles(Graphics g) {
		final int TILE_MARGIN = 400;
		try{
	    	BufferedImage image = null;
		    for(RenderTile t: allTiles){
		    	if(t.getTileType()==9){
		    		image = null;
		    	} else if(t.getTileType() == 0){
		    		image = ImageIO.read(new File(IMAGE_PATH + "crack-tile.png"));
		    	} 
		    	if(image != null){
		    		g.drawImage(image, t.X() + TILE_MARGIN, t.Y() + (TILE_MARGIN/8), this);
		    	}
		    }
		} catch(IOException e){
			e.printStackTrace();
		}

	}
}
