package kaukau.model;

import java.util.HashMap;

import kaukau.model.Room.TileType;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class GameWorld implements Serializable{

	private Room[][] map;
	private GameMap board;

	private boolean gameOver;

	/**
	 * The UID is a unique identifier for all characters in the game. This is
	 * required in order to synchronise the movements of different players
	 * across boards.
	 */
	private static int uid = 0;

	private HashMap<Integer, Player> players = new HashMap<Integer, Player>();

	public GameWorld(String filename) throws IOException{
		map = new Room[1][1];
		map[0][0] = new Room("classroom", filename);
	}

	public GameWorld() throws IOException{
		map = new Room[1][1];
		map[0][0] = new Room("classroom", "room.xml");
		gameOver = false;
	}

	public GameWorld(HashMap<Integer, Player> players) throws IOException{
		this.players = players;
		map = new Room[1][1];
	}

	/**
	 * Register a new player into the game.
	 *
	 * @return
	 */
	public synchronized int addPlayer(){
		Room room = map[0][0];
		Player player = new Player(++uid, "Player", room.getTileAt(new Point(5, 0)), Direction.EAST);
		this.players.put(uid, player);
		return uid;
	}

	/**
	 * Move player to the new position if the tile of the new position is not occupy. Otherwise, it returns False.
	 * @param name Player's name
	 * @param p The new position
	 * @return True if the player is move to the new position, otherwise False.
	 */
	public synchronized boolean movePlayer(int uid, Direction direction){
		Room room = map[0][0];
		Player player = players.get(uid);
		Tile oldPos = player.getLocation();
		Point newPos = getPointFromDirection(player, direction);

		if (board.width() <= newPos.x || board.height() <= newPos.y ||
			newPos.x < 0 || newPos.y < 0) 
			return false;

		// if the tile is emptyTile and not occupy, then move player to this new position
		Tile tile = room.getTileAt(newPos);
		if (tile.getTileType() == TileType.EMPTY && !tile.isTileOccupied()){
			oldPos.removePlayer();
			player.setLocation(tile);
			tile.addPlayer(player);
			return true;
		}
		return false;
	}

	public synchronized boolean pickupAnItem(int uid){
		Player player = players.get(uid);
		Point pos = getPointFromDirection(player, player.facingDirection());
		if (board.width() <= pos.x || board.height() <= pos.y ||
			pos.x < 0 || pos.y < 0) return false;
		
		// if the tile is EmptyTile type and item is pickupable,
		// then player can pick up the item only if there is one
		Tile tile = board.getTileAt(pos);
		if (tile.getTileType() == TileType.EMPTY && tile.containsPickupItem()){
			player.addToBag((PickupableItem)tile.getItem());
			tile.removeItem();
			return true;
		}
		return false;
	}
	
	public synchronized boolean dropAnItem(int uid){
		return false;
	}
	
	/**
	 * Get the point from the facing direction of the player.
	 * @param player the current player
	 * @param direct facing direction of the player
	 * @return new point from the facing direction of the player
	 */
	public Point getPointFromDirection(Player player, Direction direct){
		Tile oldPos = player.getLocation();
		if (direct == Direction.NORTH)
			return new Point(oldPos.X(), oldPos.Y() - 1);
		else if (direct == Direction.SOUTH)
			return new Point(oldPos.X(), oldPos.Y() + 1);
		else if (direct == Direction.EAST)
			return new Point(oldPos.X()+1, oldPos.Y());
		else return new Point(oldPos.X()-1, oldPos.Y());
	}

	/**
	 * The following method converts the current state of the game into a byte
	 * array, such that it can be shipped across a connection to an awaiting client.
	 *
	 * @return
	 * @throws IOException
	 */
	public synchronized byte[] toByteArray() throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream dataOutput = new ObjectOutputStream(bout);
		dataOutput.writeBoolean(gameOver);  // game state
		dataOutput.writeObject(this.players);
		dataOutput.writeObject(map[0][0]);
		dataOutput.flush();
		// Finally, return!!
		return bout.toByteArray();
	}

	/**
	 * The following method accepts a byte array representing the state of a
	 * game; this state will be broadcast by a master connection, and is
	 * then used to overwrite the current state (since it should be more up to date).
	 *
	 * @param bytes
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public synchronized void fromByteArray(byte[] bytes) throws IOException{
		ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
		try {
			ObjectInputStream dataInput = new ObjectInputStream(bin);
			this.gameOver = dataInput.readBoolean();
			this.players = (HashMap<Integer, Player>) dataInput.readObject();
			map[0][0] = (Room) dataInput.readObject();
		} catch (ClassNotFoundException e) {
			System.err.println("Class not found in fromByteArray. " + e);
			e.printStackTrace();
		}
	}

	/**
	 * Returns the player of the game that matched with the userId.
	 * @param uid UserId uses by the client
	 * @return the player with the matching userId
	 * @IllegalArgumentException if the userId does not contain in the game list
	 */
	public synchronized Player player(int uid){
		if (players.containsKey(uid)){
			return players.get(uid);
		}
		throw new IllegalArgumentException("Invalid Player UID");
	}

	/**
	 * Check if the game is over or not.
	 * @return true if the game is over, otherwise false.
	 */
	public synchronized Boolean gameOver(){
		return gameOver;
	}
	
	/**
	 * Return the current board of the game.
	 * @return
	 */
	public Tile[][] getGameMap(){
		return board.getBoard();
	}

	/**
	 * Return the current players of the game.
	 * @return
	 */
	public HashMap<Integer, Player> getAllPlayers(){
		return players;
	}

	/**
	 * Return the current state of the game.
	 * @return
	 */
	public boolean isOver(){
		return gameOver;
	}

	/**
	 * Return the current board of the game.
	 * @return
	 */
	public Tile[][] getGameTiles(){
		return map[0][0].getRoomBoard();
	}

	// testing
	public static void main (String[] args) throws IOException{
		/*GameWorld game = new GameWorld("room.xml");
		game.addPlayer();
		Room[][] rooms = game.getGameMap();
		Room room = rooms[0][0];

		//System.out.println(room.getTileAt(new Point(1,1)).getTileType());
		//System.out.println(game.movePlayer(player.getName(), new Point(2, 1)));
		//System.out.println(room.getTileAt(new Point(2,1)).getTileType());
		//System.out.println("move " + game.movePlayer(1, Direction.NORTH));
		//System.out.println("pickup " + game.pickupAnItem(1, new Point(6, 0)));

		Tile[][] tiles = room.getRoomBoard();
		EmptyTile item = (EmptyTile) tiles[6][0];
		System.out.println(item.containsItem());
		System.out.println(item.isTileOccupied());

		for (int x = 0; x < 7; x++){
			for (int y = 0; y < 7; y++){
				System.out.print(room.getTileAt(new Point(x,y)).getTileType() + ", ");
			}
			System.out.println();
		}*/

	}

}
