package kaukau.storage;

import java.awt.Component;
import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.xml.bind.JAXBContext;

import javax.xml.bind.JAXBException;

import javax.xml.bind.Marshaller;

import kaukau.model.Coin;
import kaukau.model.CoinBox;
import kaukau.model.Direction;
import kaukau.model.GameMap;
import kaukau.model.GameWorld;
import kaukau.model.Key;
import kaukau.model.Player;

/**
 * THis class serializes the current player and game map to XML using marshalers and unmarshalers with JAXB. The application window creates a new JavaToXml class
 * and calls the marshaller which will create a Player.xml and Map.xml file in the same directory as the project
 * @author khanshai
 * */
public class JAXBJavaToXml {

	private GameWorld game;
	private static GameMap map;
	private static Player currentPlayer;

	/**
	 * FOR TESTING
	 * Initializes a dummy game to set the current player and map
	 * */
	public void createDummyGame() {
		// create dummy game world
		this.game = new GameWorld();
		// create 2 players
		this.game.addPlayer();
		this.game.addPlayer();
		// add items already in inventory including coinBox
		HashMap<Integer, Player> players = this.game.getAllPlayers();
		this.currentPlayer = this.game.player(1);
		Player p2 = this.game.player(2);
		this.currentPlayer.addToBag(new Coin(50));
		this.currentPlayer.addToBag(new Coin(100));
		this.currentPlayer.addToBag(new Key(7));
		p2.addToBag(new Key(71));
		this.game.movePlayer(this.currentPlayer.getUserId(), Direction.NORTH);
		this.map = this.game.getGameMap();
	}

	/**
	 * Returns the dummy player
	 * @return Player
	 * */
	private static Player getPlayer(){
		return currentPlayer;
	}

	/**
	 * Returns the dummy Map
	 * @return GameMap
	 * */
	private static GameMap getMap(){
		return map;
	}

	/**
	 * calls marshalPlayer() and marshalMap() to serialize them to xml
	 * @param Player
	 * @param GameMap*/
	public void marshal(Player player, GameMap map) {
		// marshal player
		marshalPlayer(player);
		// marshal gameMap
		marshalMap(map, player.getUserId());
		//marshalGame();
	}

	/**
	 * Marshals the player passed in the argument to xml format
	 * @param Player
	 * */
	private void marshalPlayer(Player player) {
		try {
			// create JAXB context and initializing Marshaller
			JAXBContext jaxbContext = JAXBContext.newInstance(Player.class);

			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// for getting nice formatted output
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// specify the location and name of xml file to be created
			// for testing at uni
			File XMLfile = new File("Player"+player.getUserId()+".xml");
			//File XMLfile = new File("Player.xml");
			// Writing to XML file
			jaxbMarshaller.marshal(player, XMLfile);

			// Writing to console
			//jaxbMarshaller.marshal(player, System.out);

		} catch (JAXBException e) {
			// some exception occured
			e.printStackTrace();
		}
	}

	/**
	 * Marshal's the GameMap passed in the argument to xml format
	 * @param GameMap
	 * */
	private void marshalMap(GameMap map, int id) {
		try {
			// create JAXB context and initializing Marshaller
			JAXBContext jaxbContext = JAXBContext.newInstance(GameMap.class);

			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// for getting nice formatted output
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// specify the location and name of xml file to be created
			// for testing at uni
			File XMLfile = new File("Map"+id+".xml");
			//File XMLfile = new File("Map.xml");

			// Writing to XML file
			jaxbMarshaller.marshal(map, XMLfile);

			// Writing to console
			//jaxbMarshaller.marshal(map, System.out);

		} catch (JAXBException e) {
			// some exception occured
			e.printStackTrace();
		}
	}

	/**
	 * FOR TESTING
	 * Marshal's the dummy Game to xml format
	 * */
	private void marshalGame() {
		try {
			// create JAXB context and initializing Marshaller
			JAXBContext jaxbContext = JAXBContext.newInstance(GameWorld.class);

			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// for getting nice formatted output
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// specify the location and name of xml file to be created
			// for testing at uni
			File XMLfile = new File("Game.xml");

			// Writing to XML file
			jaxbMarshaller.marshal(game, XMLfile);

			// Writing to console
			//jaxbMarshaller.marshal(game, System.out);

		} catch (JAXBException e) {
			// some exception occured
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		JAXBJavaToXml marshaling = new JAXBJavaToXml();
		marshaling.createDummyGame();
		marshaling.marshal(getPlayer(), getMap());
	}

}
