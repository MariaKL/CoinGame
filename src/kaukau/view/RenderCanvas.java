package kaukau.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import kaukau.control.Client;
import kaukau.model.GameMap;
import kaukau.model.GameWorld;
import kaukau.model.Item;
import kaukau.model.Player;
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
	// game level blocks that are to be rendered
	private Block[][] levelBlocks;

	private GameWorld game;
	private HashMap<Integer, Player> players;
	private Player player;
	private Client client;

	/**This class take a gameworld parameter and
	 * creates a rendering based on the state of
	 * the game.
	 * @param game
	 */
	public RenderCanvas(GameWorld gameWorld, Player user){

		levelBlocks = new Block[20][20];

		this.game = gameWorld;
		this.setPlayers(game.getAllPlayers());
		this.player = user;

		this.setBackground(new Color(79,100,90));

		initBlocks(game);
		attachBindings();
		//set focus
		this.setFocusable(true);
		repaint();

	}

	private void setPlayers(HashMap<Integer, Player> all){
		this.players = all;
	}
	/**
	 * Sets the updated game for rendering.
	 * @param game
	 */
	public void setGame(GameWorld game){
		this.game = game;
		setPlayers(game.getAllPlayers());
		this.player = game.getAllPlayers().get(player.getUserId());
	}
	/**
	 * Associates this canvas with a client to update player actions
	 * @param client
	 */
	public void setClient(Client client){
		this.client = client;
	}

	/**initialises the blocks (tiles & walls) which make up
	 * the current level. The tiles are recieved from the
	 * passed GameWorld parameter.
	 *
	 * @param game
	 */
	public void initBlocks(GameWorld game) {

		allWalls.clear();
		allTiles.clear();
		blocks.clear();

		//Tile loc = player.getLocation();

		Tile[][] tiles = game.getGameTiles();
		for(int r=0; r!=levelBlocks.length; r++){
			for(int c=0; c!=levelBlocks[0].length; c++){
				// getting the gameworld model tile
				//Tile tile = tiles[c][r];
				Tile tile = tiles[r][c];
				// the block to be created
				Block b = null;

				// Creating a Tile block for rendering
				if(tile.getTileType() == GameMap.TileType.TILE){

					// getting the x & y position of the tile
					int x = c * tileWidth;
					int y = r * tileHeight;
					// converting 2d point to isometic
					Point pos = RenderWindow.twoDToIso(new Point(x, y));
					// adjusting the position of the render
					b = new RenderTile(0, pos.x+50, pos.y+65);
					// setting the tile item if one
					if(tile.getItem()!=null){
						((RenderTile)b).setItem(tile.getItem());
					}
					for(Map.Entry<Integer, Player> p: players.entrySet()){
						Player player = p.getValue();
						Tile loc = player.getLocation();
						if(r==loc.X()&&c==loc.Y()){
							((RenderTile)b).setPlayer(player);
						}
					}
					allTiles.add((RenderTile) b);
					// adding blocks in order for painters algorithm
					blocks.add(b);
					levelBlocks[c][r] = b;

				// creating a cracked tile block for rendering
				} else if (tile.getTileType() == GameMap.TileType.TILE_CRACKED){
					// getting the x & y position of the tile
					int x = c * tileWidth;
					int y = r * tileHeight;
					// converting 2d point to isometric
					Point pos = RenderWindow.twoDToIso(new Point(x, y));
					// adjusting the position of the render
					b = new RenderTile(1, pos.x+50, pos.y+65);
					// setting the tile item if one
					if(tile.getItem()!=null){
						((RenderTile)b).setItem(tile.getItem());
					}
					for(Map.Entry<Integer, Player> p: players.entrySet()){
						Player player = p.getValue();
						Tile loc = player.getLocation();
						if(r==loc.X()&&c==loc.Y()){
							((RenderTile)b).setPlayer(player);
						}
					}
					allTiles.add((RenderTile) b);
					// adding blocks in order for painters algorithm
					blocks.add(b);
					levelBlocks[c][r] = b;

				// Creating a Wall block for rendering
				} else if(tile.getTileType() == GameMap.TileType.WALL){
					// getting the x & y position of the tile
					int x = c * tileWidth;
					int y = r * tileHeight;
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
		    	// checking if block was a tile & contained an item
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
			    		// draw the item image if not null
			    		if(itemImg != null){
				    		g.drawImage(itemImg, b.X() + TILE_MARGIN, b.Y() + (TILE_MARGIN/8), this);
				    	}
			    	}

		    		// getting the player on this tile if one
		    		Player user = ((RenderTile)b).getPlayer();
			    	if(user != null){
			    		BufferedImage playerImg = null;
			    		switch(user.getfacingDirection()){
				    		case NORTH:
				    			playerImg = ImageIO.read(new File(IMAGE_PATH + "east1-avatar.png"));
				    			break;
				    		case EAST:
				    			playerImg = ImageIO.read(new File(IMAGE_PATH + "south1-avatar.png"));
				    			break;
				    		case SOUTH:
				    			playerImg = ImageIO.read(new File(IMAGE_PATH + "west1-avatar.png"));
				    			break;
				    		case WEST:
				    			playerImg = ImageIO.read(new File(IMAGE_PATH + "north1-avatar.png"));
				    			break;
			    		}
			    		// draw the item image if not null
			    		if(playerImg != null){
				    		g.drawImage(playerImg, b.X() + TILE_MARGIN, b.Y() + (TILE_MARGIN/8)-75, this);
				    	}
			    	}
		    	}
		    }
		} catch(IOException e) {
			e.printStackTrace();
		} catch(ConcurrentModificationException e){}
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

		// player move down
		this.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, 0), "moveDown");
		this.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_DOWN, 0), "moveDown");
		this.getActionMap().put("moveDown", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("DOWN");
				// getting players current location
				Tile oloc = player.getLocation();
				// remove player from old tile
				Block rold = levelBlocks[oloc.Y()][oloc.X()];
				((RenderTile)rold).setPlayer(null);
				// update server
				client.sendAction(KeyEvent.VK_DOWN);
				// wait for updates from server
				try {
					Thread.sleep(150);
				} catch (InterruptedException e1) {	}
				// add player to new tile
				Tile nloc = player.getLocation();
				Block rnew = levelBlocks[nloc.Y()][nloc.X()];
				((RenderTile)rnew).setPlayer(player);
				// update rendering
				//initBlocks(game);
				repaint();
			}
		});

		// player move up
		this.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_W, 0), "moveUp");
		this.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_UP, 0), "moveUp");
		this.getActionMap().put("moveUp", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				// getting players current location
				Tile oloc = player.getLocation();
				// remove player from old tile
				Block rold = levelBlocks[oloc.Y()][oloc.X()];
				((RenderTile)rold).setPlayer(null);
				// update server
				client.sendAction(KeyEvent.VK_UP);
				// wait for updates from server
				try {
					Thread.sleep(150);
				} catch (InterruptedException e1) {	}
				// add player to new tile
				Tile nloc = player.getLocation();
				Block rnew = levelBlocks[nloc.Y()][nloc.X()];
				((RenderTile)rnew).setPlayer(player);
				// update rendering
				//initBlocks(game);
				repaint();
			}
		});
		// player move left
		this.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_A, 0), "moveLeft");
		this.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_LEFT, 0), "moveLeft");
		this.getActionMap().put("moveLeft", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				// getting players current location
				Tile oloc = player.getLocation();
				// remove player from old tile
				Block rold = levelBlocks[oloc.Y()][oloc.X()];
				((RenderTile)rold).setPlayer(null);
				// update server
				client.sendAction(KeyEvent.VK_LEFT);
				// wait for updates from server
				try {
					Thread.sleep(150);
				} catch (InterruptedException e1) {	}
				// add player to new tile
				Tile nloc = player.getLocation();
				Block rnew = levelBlocks[nloc.Y()][nloc.X()];
				((RenderTile)rnew).setPlayer(player);
				// update rendering
				//initBlocks(game);
				repaint();
			}
		});
		// player move right
		this.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_D, 0), "moveRight");
		this.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_RIGHT, 0), "moveRight");
		this.getActionMap().put("moveRight", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				// getting players current location
				Tile oloc = player.getLocation();
				// remove player from old tile
				Block rold = levelBlocks[oloc.Y()][oloc.X()];
				((RenderTile)rold).setPlayer(null);
				// update server
				client.sendAction(KeyEvent.VK_RIGHT);
				// wait for updates from server
				try {
					Thread.sleep(150);
				} catch (InterruptedException e1) {	}
				// add player to new tile
				Tile nloc = player.getLocation();
				Block rnew = levelBlocks[nloc.Y()][nloc.X()];
				((RenderTile)rnew).setPlayer(player);
				// update rendering
				//initBlocks(game);
				repaint();
			}
		});
		
<<<<<<< HEAD
=======
		// enter door, if not possible give message
		this.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_E, 0), "enter");
		this.getActionMap().put("enter", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				//enter door
				game.openDoor(player.getUserId());
			}
		});
		
		//pick up object in front if possible
		this.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_P, 0), "pickup");
		this.getActionMap().put("pickup", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				//pick up object if possible
				game.pickupAnItem(player.getUserId());
				repaint();
				System.out.println("Attempt to pick up item");
				//here get direction of player and item coordinates
				//TODO: see above comment
			}
		});
		
>>>>>>> 2e9c578f2ba853d6cbd3754621db063163b4e363
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
					Item item = null;
					int type = 0;
					try{
						item = ((RenderTile)b).getItem();
						type = ((RenderTile)b).getTileType();
					} catch (NullPointerException e){ }
					// getting the tile type
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
					if(item != null){
						((RenderTile)b).setItem(item);
					}
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
