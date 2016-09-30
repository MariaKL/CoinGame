package kaukau.control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;
import java.util.Date;

import kaukau.model.GameWorld;
import kaukau.view.ApplicationWindow;

/**
 * Based off http://stackoverflow.com/questions/29545597/multiplayer-game-in-java-connect-client-player-to-game-that-was-created-by-ot
 * @author Maria Legaspi and Vivienne Yapp
 *
 */


/**
 * A client to connects
 */
public class Client extends Thread implements KeyListener{
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
		} catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public void run(){
		try {
			if(initialRun){
				// TODO: get client to read its uid from server BEFORE running client
				boolean accepted = input.readBoolean();
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
			e.printStackTrace();
			System.out.println(uid + " : uid");
		} catch (IOException e) {
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
			sock.close();
		}
		catch(IOException e){
			e.printStackTrace();
			System.out.println("Cannot close socket.");
		}
	}

	/**
	 * Send valid key events to server.
	 */
	public void keyPressed(KeyEvent e) {
		try {
			int code = e.getKeyCode();
			if(code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_KP_RIGHT) {
				System.out.println("Player " + uid + ": moved up");
				output.writeInt(uid);
				output.writeInt(code);
			} else if(code == KeyEvent.VK_LEFT || code == KeyEvent.VK_KP_LEFT) {
				output.writeInt(uid);
				output.writeInt(code);
			} else if(code == KeyEvent.VK_UP) {
				output.writeInt(uid);
				output.writeInt(code);
			} else if(code == KeyEvent.VK_DOWN) {
				output.writeInt(uid);
				output.writeInt(code);
			}
			output.flush();
		} catch(IOException ioe) {
			// something went wrong trying to communicate the key press to the
			// server.  So, we just ignore it.
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

}