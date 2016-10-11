package kaukau.control;

import kaukau.model.*;
import kaukau.view.*;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;


/**
 * Based off http://stackoverflow.com/questions/29545597/multiplayer-game-in-java-connect-client-player-to-game-that-was-created-by-ot
 * @author Maria Legaspi, 30030499
 *
 */


/**
 * A client to connects
 */
public class Client extends Thread {
	// network information
	private String address = "127.0.0.1";
	private Socket sock;
	private boolean connected;
	// stream information
	private ObjectInputStream input;
	private ObjectOutputStream output;
	// game information
	private GameWorld game;
	private ApplicationWindow aw;
	// player setup information
	private int uid;
	private boolean initialRun = true;
	// action commands
	public static final int pickUpItem = 1;
	public static final int dropItem = 2;


	/**
	 * Creates a client for the player.
	 */
	public Client(ApplicationWindow aw){
		// sets references to the game and ui window
		this.aw = aw;
		this.game = aw.getGame();
		// sets networking fields
        setupSocket();
	}

	/**
	 * Connects the socket to the server.
	 */
	private void setupSocket(){
		try{
			// sets networking fields
	        sock = new Socket(address, Server.portNumber);
	        output = new ObjectOutputStream(sock.getOutputStream());
	        input = new ObjectInputStream(sock.getInputStream());
	        connected = true;
	        System.out.println("Made socket");
	    } catch(EOFException e){
			System.out.println("Server not running.");
			System.out.println("Single player game");
		} catch(ConnectException e){
			connected = false;
			System.out.println("Server unavailable.");
			System.out.println("Single player game");
		} catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public void run(){
		try {
			// single player game
			if(!connected){
				setupSocket();
				return;
			}
			// multiplayer game
			if(initialRun){
				// checks if client is connected to the server
				boolean accepted = false;
				if(input!= null)
					accepted = input.readBoolean();
				else
					System.out.println("Client: no input stream");
				// if there were too many players then close the socket
				if(!accepted){
					System.out.println("Not accepted into server");
					return;
				}
				uid = input.readInt();
				System.out.println("New client uid: " + uid);
				initialRun = false;
			}
			// play game
			System.out.println("Waiting for game updates");
			boolean closed = false;
			while(!closed){
				System.out.println("Players size before update: " + game.getAllPlayers().size());
				// wait for game updates from server
				game.fromByteArray((byte[])input.readObject());
				// update rendering
				aw.setGame(game);
//				aw.rc.repaint();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {	}
				aw.repaint();
				System.out.println("Received game update");
				System.out.println("Players size after update: " + game.getAllPlayers().size());
			}
		} catch(EOFException e){
			e.printStackTrace();
			System.out.println("Input not read");
		} catch(ConnectException e){
			System.out.println("Server not running");
		} catch(IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Closes the sock when the server is closed.
	 */
	public void closeClientSock(){
		try{
			if(sock!=null && !sock.isClosed()){
				sock.close();
			}
		}
		catch(IOException e){
			e.printStackTrace();
			System.out.println("Cannot close socket.");
		}
	}

	/**
	 * Sends the action command to the server.
	 * @param code
	 */
	public void sendAction(int code){
		try {
			if(connected){
				output.writeInt(uid); // uid of player doing action
				output.writeInt(code); // action
				output.flush();
				System.out.println("Wrote command to server");
			}
		} catch (IOException e) {
			// problem with sending to the server
			e.printStackTrace();
		}
	}

	/**
	 * Sends action command to server with the index of a given item in the inventory.
	 * @param code
	 */
	public void sendAction(int code, int index){
		try {
			if(connected){
				output.writeInt(uid);
				output.writeInt(code); // action with item
				output.writeInt(index); // index of item in inventory
				output.flush();
				System.out.println("Wrote command to server");
			}
		} catch (IOException e) {
			// problem with sending to the server
			e.printStackTrace();
		}
	}

	/**
	 * Returns the player's uid.
	 * @return
	 */
	public int getUID(){
		return uid;
	}
}