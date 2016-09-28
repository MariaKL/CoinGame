package kaukau.model;

import java.util.HashMap;

import kaukau.model.Room.TileType;

import java.awt.Point;
import java.io.IOException;

public class GameWorld {
	
	private Room[][] map;
	private HashMap<String, Player> players;
	private final int MAX_PLAYERS = 4;
	
	public GameWorld(String filename) throws IOException{
		map = new Room[1][1];
		map[0][0] = new Room("classroom", filename);
		this.players = new HashMap<String, Player>();
	}
	
	public synchronized boolean addPlayer(String name){
		if (players.size() >= MAX_PLAYERS) return false;
		Room room = map[0][0];
		this.players.put(name, new Player(name, room.getTileAt(new Point(1, 1)), Direction.EAST));
		return true;
	}
	
	public synchronized boolean movePlayer(String name, Point p){
		Room room = map[0][0];
		if (room.width() <= p.x || room.height() <= p.y) return false;
		Player player = players.get(name);
		Tile tile = room.getTileAt(p);
		if (tile.getTileType() == TileType.EMPTY && room.isTileOccupied(p)){
			player.setLocation(tile);
			return true;
		}
		return false;
	}
	
	public synchronized boolean pickupAnItem(Player player, Point p){
		Room room = map[0][0];
		if (room.width() <= p.x || room.height() <= p.y) return false;
		Tile tile = room.getTileAt(p);
		if (room.containsItem(p)){
			return true;
		}
		return false;
	}
	
	public synchronized boolean performAction(String name, String action, Point p) {
		/*if (action.equals("Pickup")) {
			return pickupThisItem(player);
		} else if(action.equals("Drop")){
			return dropThisItem(player);
		} else {
			throw new IllegalArgumentException("Unknown action: " + action);
		}*/
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
	
	// testing
	public static void main (String[] args) throws IOException{
		GameWorld game = new GameWorld("tiles.txt");
		game.addPlayer("Vivienne");
		Room[][] rooms = game.getMap();
		Room room = rooms[0][0];
		System.out.println(room.getTileAt(new Point(1,1)).getTileType());
		System.out.println(game.getPlayer("Vivienne"));
		Player player = game.getPlayer("Vivienne");
		System.out.println(game.movePlayer(player.getName(), new Point(2, 1)));
		System.out.println(room.getTileAt(new Point(2,1)).getTileType());
		System.out.println(game.getPlayer("Vivienne"));
	}


}
