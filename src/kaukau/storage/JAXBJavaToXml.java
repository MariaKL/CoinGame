package kaukau.storage;
import java.io.File;

import java.util.ArrayList;

import javax.xml.bind.JAXBContext;

import javax.xml.bind.JAXBException;

import javax.xml.bind.Marshaller;

import kaukau.model.GameWorld;

public class JAXBJavaToXml {

	public void generateXML(GameWorld game){
		// creating country object

		   GameWorld gameWorld = game;
		   //game.addPlayer();

		  try {

		   // create JAXB context and initializing Marshaller

		   JAXBContext jaxbContext = JAXBContext.newInstance(GameWorld.class);

		   Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		   // for getting nice formatted output

		   jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		   //specify the location and name of xml file to be created

		   File XMLfile = new File("/home/khanshai/workspace/T2/Team_24/Game.xml");

		   // Writing to XML file

		   jaxbMarshaller.marshal(gameWorld, XMLfile);

		   // Writing to console

		   jaxbMarshaller.marshal(gameWorld, System.out);

		  } catch (JAXBException e) {

		   // some exception occured

		   e.printStackTrace();

		  }

	}

 public static void main(String[] args) {

  // creating country object

   GameWorld game = new GameWorld();
   //game.addPlayer();

  try {

   // create JAXB context and initializing Marshaller

   JAXBContext jaxbContext = JAXBContext.newInstance(GameWorld.class);

   Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

   // for getting nice formatted output

   jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

   //specify the location and name of xml file to be created

   File XMLfile = new File("/home/khanshai/workspace/T2/Team_24/Game.xml");

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
