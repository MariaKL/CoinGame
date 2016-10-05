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
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import kaukau.control.Client;
import kaukau.model.GameWorld;
import kaukau.model.Tile;

/**This class handles the rendering of the game levels
 * 	in the Kaukau game. This class interacts with the
 * 	application window to form the gui of the game.
 *
 * @author Patrick
 *
 */
@SuppressWarnings("serial")
public class RenderWindow extends JPanel {

	// path to the images folder
	private static final String IMAGE_PATH = "images/";
	private Client client;

	/* Field to store 2D array representation of the game level data.
	 * 	KEY:
	 *  0 - blue tile
	 * 	1..8 - cracked tile
	 * 	9 - no tile (null)
	 */
	private static int[][] levelData = {{2,2,0,0,0,0,0},
		                                {2,0,0,0,0,0,0},
		                                {2,0,0,0,0,0,0},
		                                {0,0,0,2,0,0,0},
		                                {0,0,0,2,0,0,0},
		                                {0,0,0,2,0,0,0},
		                                {2,0,0,2,0,0,0}};

	/* Field to store 2D array representation of the level sprite data.
	* KEY:
	* 1 - player
	*/
	private static int[][] spriteData = {{0,0,0,0,0,0,0},
							             {0,0,0,0,0,0,0},
							             {0,0,0,0,0,0,0},
							             {0,0,0,1,0,0,0},
							             {0,0,0,0,0,0,0},
							             {0,0,0,0,0,0,0},
							             {0,0,0,0,0,0,0}};

	/* Fields to store array representation of the level wall data.
	* KEY:
	* 1 - wall
	* 2 - door
	*/
	private static int[] northWall = {1,1,1,1,1,2,1};
	private static int[] eastWall  = {1,1,1,2,1,1,1};
	private static int[] southWall = {1,1,1,1,1,1,1};
	private static int[] westWall  = {1,1,1,1,1,1,1};

	// Field to store the board margins in pixels
	private static final int MARGIN = 324;
	private static final int SPRITE_MARGIN = 16;
	private static final int WALL_Y = -66;

	// Fields to store the tile width & height in pixels
	private static final int tileWidth = 50;
	private static final int tileHeight = 50;

	// Field to store all the tiles in the current level
	private List<kaukau.view.Tile> allTiles = new ArrayList<kaukau.view.Tile>();
	// Field to store all the sprites in the current level
	private List<Sprite> allSprites = new ArrayList<Sprite>();

	// Field to store the current direction of the board
	private char gameDir = 'S';

	// Field to store the current direction of the player
	private char playerDir = 'S';

	// Field to store the current player
	Sprite player;
	
	// Field to store GameWorld object
	GameWorld game;

	public RenderWindow(GameWorld game){
		this.game = game;
		//Setting a border
		// FIXME: Stop border resizing with window
		setBorder(BorderFactory.createCompoundBorder(
						      BorderFactory.createEmptyBorder(0, 2, 2, 2),
						      BorderFactory.createLineBorder(Color.BLACK, 2)
						   ));

		// initilising the level from 2D level data array
		initLevel();
		// initilising level sprites from 2D sprite data array
		initSprites();
		// attach keys to keyboard actions
		attachBindings();
		// repaint board
		repaint();
	}

	/**
	 * Initilising the game sprites on the current level
	 *  from the 2D sprite data array.
	 */
	private void initSprites() {
		for (int i = 0; i<spriteData.length; i++){
			for (int j = 0; j<spriteData[0].length; j++){

				// getting the x & y position of the tile
				int x = j * tileWidth;
				int y = i * tileHeight;

				// getting the type type
				int spriteType = spriteData[i][j];
				if(spriteType == 0) continue;

				// creating and adding the tile to the current level board
				placeSprite(spriteType, twoDToIso(new Point(x, y)));
			}
		}
	}

	/**This method creates and adds a sprite to a list
	 *  of all sprites which make up the current game level.
	 *
	 * @param spriteType
	 * @param pt
	 */
	private void placeSprite(int spriteType, Point pt) {
		// Creating a new sprite
		Sprite s = new Sprite(spriteType, pt.x, pt.y);
		if(spriteType == 1) this.player = s;
		// Adding the sprite to the current level
		allSprites.add(s);
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
		kaukau.view.Tile t = new kaukau.view.Tile(tileType, pt.x, pt.y);
		// Adding the tile to the current level
		allTiles.add(t);
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
				rotateSprites();
				repaint();
			}
		});

		//player move up
		this.getInputMap().put(KeyStroke.getKeyStroke(
				KeyEvent.VK_UP, 0), "moveUp");
		this.getInputMap().put(KeyStroke.getKeyStroke(
				KeyEvent.VK_W, 0), "moveUp");
		this.getActionMap().put("moveUp", new AbstractAction() {
			public void actionPerformed(ActionEvent e){
				// TODO: check if action is legal

				// update player direction
				playerDir = 'E';

				// update sprite array position
				Point p = player.getTilePos();
				Point updatedPos = new Point(p.x, p.y - 1);
				player.setPosFromTilePos(updatedPos);
				// update 2d array position
				int r1 = 0;
				int c1 = 0;
				for (int r=0;r<spriteData.length;r++){
					for (int c=0;c<spriteData[0].length;c++){
						if (spriteData[r][c] == 1){
							r1 = r;
							c1 = c;
							spriteData[r][c] = 0;
						}
					}
				}
				spriteData[r1-1][c1] = 1;
				client.sendAction(KeyEvent.VK_UP);
				repaint();
			}
		});

		//player move down
		this.getInputMap().put(KeyStroke.getKeyStroke(
				KeyEvent.VK_DOWN, 0), "moveDown");
		this.getInputMap().put(KeyStroke.getKeyStroke(
				KeyEvent.VK_S, 0), "moveDown");
		this.getActionMap().put("moveDown", new AbstractAction() {
			public void actionPerformed(ActionEvent e){
				// TODO: check if action is legal

				// update player direction
				playerDir = 'W';

				// update sprite array position
				Point p = player.getTilePos();
				Point updatedPos = new Point(p.x, p.y + 1);
				player.setPosFromTilePos(updatedPos);
				// update 2d array position
				int r1 = 0;
				int c1 = 0;
				for (int r=0;r<spriteData.length;r++){
					for (int c=0;c<spriteData[0].length;c++){
						if (spriteData[r][c] == 1){
							r1 = r;
							c1 = c;
							spriteData[r][c] = 0;
						}
					}
				}
				spriteData[r1+1][c1] = 1;
				client.sendAction(KeyEvent.VK_DOWN);
				repaint();
			}
		});

		//player move left
		this.getInputMap().put(KeyStroke.getKeyStroke(
				KeyEvent.VK_RIGHT, 0), "moveRight");
		this.getInputMap().put(KeyStroke.getKeyStroke(
				KeyEvent.VK_D, 0), "moveRight");
		this.getActionMap().put("moveRight", new AbstractAction() {
			public void actionPerformed(ActionEvent e){
				// TODO: check if action is legal

				// update player direction
				playerDir = 'S';

				// update sprite array position
				Point p = player.getTilePos();
				Point updatedPos = new Point(p.x+1, p.y );
				player.setPosFromTilePos(updatedPos);
				// update 2d array position
				int r1 = 0;
				int c1 = 0;
				for (int r=0;r<spriteData.length;r++){
					for (int c=0;c<spriteData[0].length;c++){
						if (spriteData[r][c] == 1){
							r1 = r;
							c1 = c;
							spriteData[r][c] = 0;
						}
					}
				}
				spriteData[r1][c1+1] = 1;
				client.sendAction(KeyEvent.VK_RIGHT);
				repaint();
			}
		});

		//player move right
		this.getInputMap().put(KeyStroke.getKeyStroke(
				KeyEvent.VK_LEFT, 0), "moveLeft");
		this.getInputMap().put(KeyStroke.getKeyStroke(
				KeyEvent.VK_A, 0), "moveLeft");
		this.getActionMap().put("moveLeft", new AbstractAction() {
			public void actionPerformed(ActionEvent e){
				// TODO: check if action is legal

				// update player direction
				playerDir = 'N';

				// update sprite array position
				Point p = player.getTilePos();
				Point updatedPos = new Point(p.x-1, p.y);
				player.setPosFromTilePos(updatedPos);
				// update 2d array position
				int r1 = 0;
				int c1 = 0;
				for (int r=0;r<spriteData.length;r++){
					for (int c=0;c<spriteData[0].length;c++){
						if (spriteData[r][c] == 1){
							r1 = r;
							c1 = c;
							spriteData[r][c] = 0;
						}
					}
				}
				spriteData[r1][c1-1] = 1;
				client.sendAction(KeyEvent.VK_LEFT);
				repaint();
			}
		});
	}

	/**
	 * rotate game world 90 degrees
	 */
	public void rotateWorld() {
		//first, rotate game direction
		switch(gameDir){
		case 'N':
			gameDir = 'E';
			break;
		case 'E':
			gameDir = 'S';
			break;
		case 'S':
			gameDir = 'W';
			break;
		case 'W':
			gameDir = 'N';
			break;
		}
		//rotate int[][] 90 degrees into new 2d array
		final int M = levelData.length;
	    final int N = levelData[0].length;
	    int[][] ret = new int[N][M];
	    for (int r = 0; r < M; r++) {
	        for (int c = 0; c < N; c++) {
	            ret[c][M-1-r] = levelData[r][c];
	        }
	    }
	    //clear old list, insert new tile arrangement
	    allTiles.clear();
	    for (int i = 0; i<ret.length; i++){
			for (int j = 0; j<ret[0].length; j++){

				// getting the x & y position of the tile
				int x = j * tileWidth;
				int y = i * tileHeight;

				// getting the tile type
				int tileType = ret[i][j];

				// creating and adding the tile to the current level board
				placeTile(tileType, twoDToIso(new Point(x, y)));
			}
	    }
	    //replace levelData with new 2d array for future rotations
	    for(int a=0; a<levelData.length; a++)
	    	  for(int b=0; b<levelData[0].length; b++)
	    	    levelData[a][b]=ret[a][b];
	}

	/**
	 * rotate game world 90 degrees
	 */
	public void rotateSprites() {
		//first, rotate player direction
		switch(playerDir){
		case 'N':
			playerDir = 'E';
			break;
		case 'E':
			playerDir = 'S';
			break;
		case 'S':
			playerDir = 'W';
			break;
		case 'W':
			playerDir = 'N';
			break;
		}
		//rotate int[][] 90 degrees into new 2d array
		final int M = spriteData.length;
	    final int N = spriteData[0].length;
	    int[][] ret = new int[N][M];
	    for (int r = 0; r < M; r++) {
	        for (int c = 0; c < N; c++) {
	            ret[c][M-1-r] = spriteData[r][c];
	        }
	    }
	    //clear old list, insert new tile arrangement
	    allSprites.clear();
	    for (int i = 0; i<ret.length; i++){
			for (int j = 0; j<ret[0].length; j++){

				// getting the x & y position of the tile
				int x = j * tileWidth;
				int y = i * tileHeight;

				// getting the tile type
				int spriteType = ret[i][j];

				// creating and adding the tile to the current level board
				placeSprite(spriteType, twoDToIso(new Point(x, y)));
			}
	    }
	    //replace spriteData with new 2d array for future rotations
	    for(int a=0; a<spriteData.length; a++)
	    	  for(int b=0; b<spriteData[0].length; b++)
	    	    spriteData[a][b]=ret[a][b];
	}

	@Override
	public void paintComponent(Graphics g) {
	     super.paintComponent(g);

	     try {

	    	//FIXME: drawing walls from the wall arrays

	    	// creating wall images
	    	BufferedImage northWallImg = ImageIO.read(new File(IMAGE_PATH + "north-wall.png"));
	    	BufferedImage eastWallImg = ImageIO.read(new File(IMAGE_PATH + "east-wall.png"));
	    	// creating locked door images
	    	BufferedImage northLockedDoorImg = ImageIO.read(new File(IMAGE_PATH + "north-locked-door.png"));
	    	BufferedImage eastLockedDoorImg = ImageIO.read(new File(IMAGE_PATH + "east-locked-door.png"));
	    	// creating door images
	    	BufferedImage northDoorImg = ImageIO.read(new File(IMAGE_PATH + "north-door.png"));
	    	BufferedImage eastDoorImg = ImageIO.read(new File(IMAGE_PATH + "east-door.png"));

	    	// wall positioning values
	    	int nx = 284; // stores the starting x pos for the north walls
	    	int ex = 356; // stores the starting x pos for the east walls
	    	int ny = WALL_Y, ey = WALL_Y; // stores the starting y pos for the north & east walls

	    	// FIXME: position doors without these points
	    	// door positioning values
	    	Point doorN = new Point(0,0); // stores the position of the locked door on the north wall
	    	Point doorE = new Point(0,0); // stores the position of the locked door on the east wall

	    	// draw walls based on current game view
	    	switch(gameDir){
	    		case 'S':
	    			// draw north and east walls
	    			for(int i=0; i<=northWall.length; i++){
	    				if(i<northWall.length && northWall[i] != 1){
	    					doorN.x = nx;
	    					doorN.y = ny;
	    				}
	    				if(i<eastWall.length && eastWall[i] != 1){
	    					doorE.x = ex;
	    					doorE.y = ey;
	    				}
	    				// drawing walls
	    	    		g.drawImage(northWallImg, nx, ny, this);
	    	    		g.drawImage(eastWallImg, ex, ey, this);
	    	    		nx = nx - 42;
	    	    		ny = ny + 22;
	    	    		ex = ex + 43;
	    	    		ey = ey + 22;
	    			}
	    			// drawing doors
	    			g.drawImage(northLockedDoorImg, doorN.x+(SPRITE_MARGIN/4), doorN.y+(SPRITE_MARGIN/2), this);
	    			g.drawImage(eastDoorImg, doorE.x+(SPRITE_MARGIN*2+4), doorE.y+(SPRITE_MARGIN+8), this);
	    			break;
	    		case 'W':
	    			// draw west and north walls
	    			for(int i=0; i<=westWall.length; i++){
	    				if(i<westWall.length && westWall[i] != 1){
	    					doorN.x = nx;
	    					doorN.y = ny;
	    				}
	    				if(i<northWall.length && northWall[i] != 1){
	    					doorE.x = ex;
	    					doorE.y = ey;
	    				}
	    				// drawing walls
	    	    		g.drawImage(northWallImg, nx, ny, this);
	    	    		g.drawImage(eastWallImg, ex, ey, this);
	    	    		nx = nx - 42;
	    	    		ny = ny + 22;
	    	    		ex = ex + 43;
	    	    		ey = ey + 22;
	    			}
	    			// drawing doors
	    			//g.drawImage(northDoorImg, doorN.x+(SPRITE_MARGIN), doorN.y+(SPRITE_MARGIN+8), this);
	    			g.drawImage(eastLockedDoorImg, doorE.x-(SPRITE_MARGIN*9), doorE.y-(SPRITE_MARGIN*4), this);
	    			break;
	    		case 'N':
	    			// draw south and west walls
	    			for(int i=0; i<=southWall.length; i++){
	    				if(i<westWall.length && westWall[i] != 1){
	    					doorE.x = ex;
	    					doorE.y = ey;
	    				}
	    				// drawing walls
	    	    		g.drawImage(northWallImg, nx, ny, this);
	    	    		g.drawImage(eastWallImg, ex, ey, this);
	    	    		nx = nx - 42;
	    	    		ny = ny + 22;
	    	    		ex = ex + 43;
	    	    		ey = ey + 22;
	    			}
	    			// drawing doors
	    			//g.drawImage(eastDoorImg, doorE.x+(SPRITE_MARGIN*2+4), doorE.y+(SPRITE_MARGIN+8), this);
	    			break;
	    		case 'E':
	    			// draw east and south walls
	    			for(int i=0; i<=eastWall.length; i++){
	    				if(i<eastWall.length && eastWall[i] != 1){
	    					doorN.x = nx;
	    					doorN.y = ny;
	    				}
	    				// drawing walls
	    	    		g.drawImage(northWallImg, nx, ny, this);
	    	    		g.drawImage(eastWallImg, ex, ey, this);
	    	    		nx = nx - 42;
	    	    		ny = ny + 22;
	    	    		ex = ex + 43;
	    	    		ey = ey + 22;
	    			}
	    			// drawing doors
	    			g.drawImage(northDoorImg, doorN.x+(SPRITE_MARGIN), doorN.y+(SPRITE_MARGIN+8), this);
	    			break;
	    	}

			// TODO: tile randomisation.
	    	// Drawing all level tiles onto the rendering panel
	    	BufferedImage image;
		    for(kaukau.view.Tile t: allTiles){
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

		    // Drawing the game sprites onto the level
		    // TODO: More sprites and player animation
		    for(Sprite s: allSprites){
		    	image = null;
		    	if(s.getSpriteType() == 1){
		    		switch(playerDir){
		    			case 'N':
		    				image = ImageIO.read(new File(IMAGE_PATH + "north1-avatar.png"));
		    				break;
		    			case 'E':
		    				image = ImageIO.read(new File(IMAGE_PATH + "east1-avatar.png"));
		    				break;
		    			case 'S':
		    				image = ImageIO.read(new File(IMAGE_PATH + "south1-avatar.png"));
		    				break;
		    			case 'W':
		    				image = ImageIO.read(new File(IMAGE_PATH + "west1-avatar.png"));
		    				break;
		    		}
		    		if(image != null){
		    			g.drawImage(image, s.X() + (720/2) - (SPRITE_MARGIN*2), s.Y() - (SPRITE_MARGIN/3-3), this);
		    		}
		    	}
		    }

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
	static Point twoDToIso (Point pt){
		Point result = new Point(0,0);
		result.x = pt.x - pt.y;
		result.y = (pt.x + pt.y) / 2;
		return(result);
	}
	/**
     * convert an isometric point to 2D
     * */
	static Point isoTo2D(Point pt) {
		Point result = new Point(0, 0);
		result.x = (2 * pt.y + pt.x) / 2;
		result.y = (2 * pt.y - pt.x) / 2;
		return(result);
	}
    /**
     * convert a 2d point to specific tile row/column
     * */
    static Point getTileCoordinates(Point pt, int tileHeight) {
        Point result = new Point(0,0);
        result.x=(int) Math.floor(pt.x/tileHeight);
        result.y=(int) Math.floor(pt.y/tileHeight);
        return(result);
    }
    /**
     * convert specific tile row/column to 2d point
     * */
    static Point get2dFromTileCoordinates(Point pt, int tileHeight) {
        Point result = new Point(0,0);
        result.x=pt.x*tileHeight;
        result.y=pt.y*tileHeight;
        return(result);
    }

    /**
     * Stores a reference to the client to pass player actions through to server.
     * @param client
     */
    public void addClient(Client client){
    	this.client = client;
    }
    
    /**
	 * Get 2d int array for render window to render tiles
	 * @return
	 */
	public int[][] getTiles4Render() {
		Tile[][] tiles = game.getGameTiles();
		for (int i=0;i<tiles.length;i++){
			for (int j=0;j<tiles[0].length;j++){
				if (tiles[i][j].getTileType()==kaukau.model.GameMap.TileType.EMPTY){
					//empty tile is 0
					levelData[i-1][j-1] = 0; //shift over to account for walls in tile map
				} else if (tiles[i][j].getTileType()==kaukau.model.GameMap.TileType.WALL){
					// Wall is 1
					if (i==0 && j>0){
						//north
						northWall[j-1]=1;
					} else if (j==6 && i>0){
						//east
						eastWall[i-1]=1;
					} else if (i==6 && j>0){
						//south
						southWall[j-1]=1;
					} else if (j==0 && i>0){
						//west
						westWall[i-1]=1;
					} 
				} else if (tiles[i][j].getTileType()==kaukau.model.GameMap.TileType.DOOR){
					// Door is 2
					if (i==0 && j>0){
						//north
						northWall[j-1]=2;
					} else if (j==6 && i>0){
						//east
						eastWall[i-1]=2;
					} else if (i==6 && j>0){
						//south
						southWall[j-1]=2;
					} else if (j==0 && i>0){
						//west
						westWall[i-1]=2;
					} 
				}
			}
		}
		return null;
	}
	
	/**
	 * Get 2d int array for render window to render players
	 * @return
	 */
	public int[][] getPlayers4Render() {
		
		return null;
	}
}