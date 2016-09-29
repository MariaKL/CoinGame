package kaukau.model;

import java.util.HashMap;

import kaukau.model.Room.TileType;
import kaukau.storage.ReadXMLFile;

import java.awt.Point;
import java.io.IOException;
import org.w3c.dom.Document;


public class GameWorld {

	private Room[][] map;
	private HashMap<String, Player> players = new HashMap<String, Player>();
	private final int MAX_PLAYERS = 4;

	public GameWorld(String filename) throws IOException{
		map = new Room[1][1];
		map[0][0] = new Room("classroom", filename);
	}

	/**
	 * Add new player to the current game. If the name of player is already in the list, it returns False.
	 * @param name
	 * @return True if the player was successfully added, otherwise False.
	 */
	public synchronized boolean addPlayer(String name){
		if (players.size() >= MAX_PLAYERS || players.containsKey(name)) return false;
		Room room = map[0][0];
		this.players.put(name, new Player(name, room.getTileAt(new Point(5, 0)), Direction.EAST));
		return true;
	}

	/**
	 * Move player to the new position if the tile of the new position is not occupy. Otherwise, it returns False.
	 * @param name Player's name
	 * @param p The new position
	 * @return True if the player is move to the new position, otherwise False.
	 */
	public synchronized boolean movePlayer(String name, Point p){
		Room room = map[0][0];
		if (room.width() <= p.x || room.height() <= p.y) return false;
		Player player = players.get(name);
		Tile tile = room.getTileAt(p);

		// if the tile is emptyTile and not occupy, then move player to this new position
		if (tile.getTileType() == TileType.EMPTY){
			EmptyTile e = (EmptyTile) tile;
			EmptyTile oldPos = (EmptyTile) player.getLocation();
			if (!e.isTileOccupied()){
				oldPos.removePlayer();
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

	public synchronized boolean performAction(String name, String action, Point p) {
		return false;
	}

	private Room[][] getMap(){
		return map;
	}

	private Player getPlayer(String name){
		if (players.containsKey(name)){
			return players.get(name);
		}
		return null;
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
		GameWorld game = new GameWorld("room.xml");
		game.addPlayer("Vivienne");
		Room[][] rooms = game.getMap();
		Room room = rooms[0][0];

		System.out.println(room.getTileAt(new Point(1,1)).getTileType());
		System.out.println(game.getPlayer("Vivienne"));
		Player player = game.getPlayer("Vivienne");
		System.out.println(player.facingDirection());
		//System.out.println(game.movePlayer(player.getName(), new Point(2, 1)));
		//System.out.println(room.getTileAt(new Point(2,1)).getTileType());
		System.out.println("move " + game.movePlayer(player.getName(), new Point(6, 0)));
		System.out.println(game.pickupAnItem(player.getName(), new Point(6, 0)));
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
		}
	}

}
