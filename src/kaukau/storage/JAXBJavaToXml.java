package kaukau.storage;

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;

import javax.xml.bind.JAXBException;

import javax.xml.bind.Marshaller;

import kaukau.model.Coin;
import kaukau.model.GameWorld;
import kaukau.model.Key;
import kaukau.model.Player;

public class JAXBJavaToXml {

	public void generateXML(GameWorld game) {
		// creating country object

		GameWorld gameWorld = game;
		// game.addPlayer();

		try {

			// create JAXB context and initializing Marshaller

			JAXBContext jaxbContext = JAXBContext.newInstance(GameWorld.class);

			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// for getting nice formatted output

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// specify the location and name of xml file to be created

			File XMLfile = new File("/home/khanshai/workspace/T2/Team_24/Game.xml");

			// Writing to XML file

			jaxbMarshaller.marshal(gameWorld, XMLfile);

			// // Writing to console
			//
			// jaxbMarshaller.marshal(gameWorld, System.out);

		} catch (JAXBException e) {

			// some exception occured

			e.printStackTrace();

		}

	}

	public static GameWorld createDummyGame() {
		// create dummy game world
		GameWorld game = new GameWorld();
		// create 2 players
		game.addPlayer();
		game.addPlayer();
		// add items already in inventory including coinBox
		HashMap<Integer, Player> players = game.getAllPlayers();
		Player p1 = game.player(1);
		Player p2 = game.player(2);
		p1.addToBag(new Coin(50));
		p1.addToBag(new Coin(100));
		p1.addToBag(new Key(7));
		p2.addToBag(new Key(71));

		return game;
	}

	public static void main(String[] args) {

		// creating country object

		GameWorld game = createDummyGame();
		// game.addPlayer();

		try {

			// create JAXB context and initializing Marshaller

			JAXBContext jaxbContext = JAXBContext.newInstance(GameWorld.class);

			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// for getting nice formatted output

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// specify the location and name of xml file to be created
			// for testing at uni
			// File XMLfile = new File("/home/khanshai/workspace/T2/Team_24/Game.xml");
			// for testing at home
			File XMLfile = new File("/C:/Oishi/WORK/Work/2016/TRI 2/SWEN 222/PROJECT/Team_24/Game.xml");
			// Writing to XML file

			jaxbMarshaller.marshal(game, XMLfile);

			// Writing to console

			jaxbMarshaller.marshal(game, System.out);

		} catch (JAXBException e) {

			// some exception occured

			e.printStackTrace();

		}

	}

}
