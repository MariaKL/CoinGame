package kaukau.storage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;

import javax.xml.bind.JAXBException;

import javax.xml.bind.Unmarshaller;

import kaukau.model.GameWorld;
import kaukau.model.Player;

public class JAXBXMLToJava {

	public GameWorld toJava(){
		return null;
	}

	public static void main(String[] args) {

  try {

   // create JAXB context and initializing Marshaller

   JAXBContext jaxbContext = JAXBContext.newInstance(GameWorld.class);

   Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

   // specify the location and name of xml file to be read

   File XMLfile = new File("/home/khanshai/workspace/T2/Team_24/Game.xml");

   // this will create Java object - country from the XML file

   GameWorld game = (GameWorld) jaxbUnmarshaller.unmarshal(XMLfile);

   HashMap<Integer, Player> players = game.getAllPlayers();

   System.out.println("Number of players: " + players.values().size());

   for(Player p : players.values()){
	   System.out.println("Player name: "+p.getName());
	   System.out.println("Player id: "+p.getUserId());
	   System.out.println("Player name: "+p.facingDirection());
   }

  } catch (JAXBException e) {

   // some exception occured

   e.printStackTrace();

  }

 }

}
