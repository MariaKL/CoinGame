package kaukau.model;

import java.util.HashMap;

import kaukau.model.Room.TileType;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class GameWorld {
	
	private Room[][] map;
	
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
	 * Add new player to the current game. If the name of player is already in the list, it returns False.
	 * @param name
	 * @return True if the player was successfully added, otherwise False.
	 *//*
	public synchronized boolean addPlayer(String name){
		if (players.size() >= MAX_PLAYERS || players.containsKey(name)) return false;
		Room room = map[0][0];
		this.players.put(name, new Player(++uid, name, room.getTileAt(new Point(5, 0)), Direction.EAST));
		return true;
	}*/
	
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
		Point newPos;
		if (direction == Direction.NORTH)
			newPos = new Point(oldPos.X(), oldPos.Y() - 1); 
		else if (direction == Direction.SOUTH)
			newPos = new Point(oldPos.X(), oldPos.Y() + 1); 
		else if (direction == Direction.EAST)
			newPos = new Point(oldPos.X()+1, oldPos.Y()); 
		else newPos = new Point(oldPos.X()-1, oldPos.Y()); 

		if (room.width() <= newPos.x || room.height() <= newPos.y) return false;

		// if the tile is emptyTile and not occupy, then move player to this new position
		Tile tile = room.getTileAt(newPos);
		if (tile.getTileType() == TileType.EMPTY){
			EmptyTile e = (EmptyTile) tile;
			EmptyTile o = (EmptyTile) player.getLocation();
			if (!e.isTileOccupied()){
				o.removePlayer();
				player.setLocation(tile);
				e.addPlayer(player);
				return true;
			}
		}
		return false;
	}

	public synchronized boolean pickupAnItem(String name, Point p){
		Room room = map[0][0];
		if (room.width() <= p.x || room.height() <= p.y) return false;
		Player player = players.get(name);
		Tile tile = room.getTileAt(p);
		// if the tile is EmptyTile type, 
		// then player can pick up item only if there is an item
		if (tile instanceof EmptyTile){
			EmptyTile e = (EmptyTile) tile;
			if (e.containsItem()){
				PickupableItem item = e.getItem();
				player.addToBag(item);
				item.setOwner(player);
				e.removeItem();
				return true;
			}
		}
		return false;
	}
	
	public synchronized boolean dropAnItem(int uid, Point p){
		Room room = map[0][0];
		if (room.width() <= p.x || room.height() <= p.y) return false;
		Player player = players.get(uid);
		Tile tile = room.getTileAt(p);
		// if the tile is EmptyTile type, 
		// then player can pick up item only if there is an item
		if (tile instanceof EmptyTile){
			EmptyTile e = (EmptyTile) tile;
			if (!e.containsItem()){
				PickupableItem item = e.getItem();
				player.addToBag(item);
				item.setOwner(player);
				e.removeItem();
				return true;
			}
		}
		return false;
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
	 * /**
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
	 * Return the current board of the game.
	 * @return
	 */
	public Room[][] getGameMap(){
		return map;
	}

	// testing
	public static void main (String[] args) throws IOException{
		/*GameWorld game = new GameWorld("room.xml");
		game.addPlayer("Vivienne");
		Room[][] rooms = game.getGameMap();
		Room room = rooms[0][0];

		System.out.println(room.getTileAt(new Point(1,1)).getTileType());
		System.out.println(game.getPlayer("Vivienne"));
		Player player = game.getPlayer("Vivienne");
		System.out.println(player.facingDirection());
		//System.out.println(game.movePlayer(player.getName(), new Point(2, 1)));
		//System.out.println(room.getTileAt(new Point(2,1)).getTileType());
		System.out.println("move " + game.movePlayer(player.getName(), new Point(6, 0)));
		System.out.println("pickup " + game.pickupAnItem(player.getName(), new Point(6, 0)));
		System.out.println(player);
		System.out.println(player.facingDirection());

		for (Item item : player.getBag().getStorage()){
			System.out.println(item.getName());
		}

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
