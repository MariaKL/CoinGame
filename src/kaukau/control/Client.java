package kaukau.control;

import kaukau.model.*;
import kaukau.view.*;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Date;


/**
 * Based off http://stackoverflow.com/questions/29545597/multiplayer-game-in-java-connect-client-player-to-game-that-was-created-by-ot
 * @author Maria Legaspi and Vivienne Yapp
 *
 */


/**
 * A client to connects
 */
public class Client extends Thread {
	private String address = "127.0.0.1";
	private Socket sock;
	private GameWorld game;
	private ApplicationWindow aw;
//	private Server server;

	private ObjectInputStream input;
	private ObjectOutputStream output;

	private int uid;

	private boolean initialRun = true;


	/**
	 * Creates a client for the player.
	 */
	public Client(ApplicationWindow aw){
		// makes a new socket and sets input/output streams
		try{
	        sock = new Socket(address, Server.portNumber);
	        input = new ObjectInputStream(sock.getInputStream());
			output = new ObjectOutputStream(sock.getOutputStream());
	    } catch(EOFException e){
			System.out.println("Server not running.");
		} catch(ConnectException e){
			System.out.println("Server not running");
		} catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public void run(){
		try {
			if(initialRun){
				// TODO: get client to read its uid from server BEFORE running client

				// checks if client is connected to the server
				boolean accepted = false;
				if(input!=null)
					accepted = input.readBoolean();
				// if there were too many players then close the socket
				if(!accepted){
					sock.close();
					System.out.println("Not accepted into server");
					return;
				}
				uid = input.readInt();
				System.out.println("New client uid: " + uid);
				game = (GameWorld)input.readObject();
				initialRun = false;
			}
			boolean closed = false;
			while(!closed){
				// wait for game updates from server
				GameWorld updatedGame = (GameWorld)input.readObject();
				System.out.println("Received game update");
			}
			sock.close();
		} catch(EOFException e){
			System.out.println("UID not read");
			System.out.println(uid + " : uid");
		} catch(ConnectException e){
			System.out.println("Server not running");
		} catch(IOException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Closes the sock when the server is closed.
	 */
	public void closeClientSock(){
		try{
			sock.close();
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
			output.writeInt(uid);
			output.writeInt(code);
		} catch (IOException e) {
			// problem with sending to the server
//			e.printStackTrace();
		}
	}
}