package kaukau.control;

import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import kaukau.model.Direction;
import kaukau.model.GameWorld;
import kaukau.view.ApplicationWindow;

/**
 * Based off http://stackoverflow.com/questions/29545597/multiplayer-game-in-java-connect-client-player-to-game-that-was-created-by-ot
 * @author Maria Legaspi
 *
 */

public class Server extends Thread{
	private String address = "127.0.0.1";
	// port as all clients should be able to access this port
	public static int portNumber = 4000;
	// server sockets
	private ServerSocket listener; // accepts clients
	// client sockets
	private static HashMap<Integer, Socket> sockets = new HashMap<Integer, Socket>();
	// client threads associated with sockets
	private Set<ClientThread> threads = new HashSet<ClientThread>();
	// commands received from clients
	private Queue<String> commands = new LinkedList<String>();
	// game
	private static GameWorld game;

	private static ObjectInputStream input;
	private static ObjectOutputStream output;

	public Server(GameWorld game){
		this.game = game;
        try {
			listener = new ServerSocket(portNumber);
//			input = new ObjectInputStream(listener.getInputStream());
//			output = new ObjectOutputStream(listener.getOutputStream());

			System.out.println("Created server");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void runGame() throws Exception{
		while(atleastOneConnection()) {
//			game.setState(Board.READY);
			Thread.sleep(3000);
//			game.setState(Board.PLAYING);
			// now, wait for the game to finish
			while(!game.isOver()) {
				// listen to clients
				// add command to queue
				// read the next command and process
				int uid = input.readInt();
				int dir = input.readInt();
				switch(dir) {
					case KeyEvent.VK_UP:
					case KeyEvent.VK_KP_UP:
						game.movePlayer(uid, Direction.NORTH);
						System.out.println(uid + ": Moved north");
						break;
					case KeyEvent.VK_DOWN:
					case KeyEvent.VK_KP_DOWN:
						game.movePlayer(uid, Direction.SOUTH);
						break;
					case KeyEvent.VK_RIGHT:
					case KeyEvent.VK_KP_RIGHT:
						game.movePlayer(uid, Direction.EAST);
						break;
					case KeyEvent.VK_LEFT:
					case KeyEvent.VK_KP_LEFT:
						game.movePlayer(uid, Direction.WEST);
						break;
				}
				// send back to clients
			}
			// If we get here, then we're in game over mode
			Thread.sleep(3000);
//			// Reset board state
//			game.setState(Board.WAITING);
//			game.fromByteArray(state);
			updateAll();
		}
	}

	/**
	 * Checks there's at least one socket that's connected.
	 * @return
	 */
	private static boolean atleastOneConnection() {
		for (Socket s : sockets.values()) {
			if (s.isConnected()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Accepts clients wanting to connect to the server.
	 * @throws Exception
	 */
    public void run(){
        try{
        	System.out.println("Listening");
            while(true){
				Socket socket = listener.accept();
				// if there are more than two players then don't accept the new player
            	if(sockets.size() > 2){
    				output = new ObjectOutputStream(socket.getOutputStream());
    				output.writeBoolean(false);
    				output.flush();
    				output.writeUTF("Cannot accept another player");
    	        	output.flush();
            	}
            	// otherwise accept a new player
            	else{
					// accept a new client
					int uid = game.addPlayer();
					sockets.put(uid, socket);
					System.out.println("New socket: " + socket.getPort());

					// send the client's uid to the client
					output = new ObjectOutputStream(socket.getOutputStream());
					output.write(uid);
		        	output.flush();
		        	// send the game to the client
		        	output.writeObject(game);
		        	output.flush();

		        	System.out.println("ALL CLIENTS ACCEPTED --- GAME BEGINS");
	//				// make a thread associated with the socket
	//				ClientThread clientThread = new ClientThread(this, socket);
	//				threads.add(clientThread);
	//				clientThread.start();
            	}
            }
        }
        catch(IOException e){
        	e.printStackTrace();
        	System.out.println("Failed to accept client on port " + portNumber + "\n");
        }
        finally {
        	try{
        		listener.close();
        	}
        	catch(IOException e){
        		e.printStackTrace();
        	}
        }
    }

    /**
     * Sends updated game to all clients.
     * @param message
     */
    public static void updateAll(){
    	try{
          for(Socket sock: sockets.values()){
        	  // sends game to byte array to each client
        	  DataOutputStream out = new DataOutputStream(sock.getOutputStream());
        	  out.write(game.toByteArray());
        	  out.flush();
          }
    	}
    	catch(IOException e){
    		System.out.println("Failed to create buffered writer.\n");
    	}
    }

    /**
     * Starts a game and associates it with a server.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException{
    	if(args.length != 1){
    		throw new IOException("Missing game file input.");
    	}
		// call gameworld with file
    	GameWorld newGame = new GameWorld(args[0]);
    	// make and run a server
    	new Server(newGame).start();
	}
}
