package kaukau.control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Based off http://stackoverflow.com/questions/29545597/multiplayer-game-in-java-connect-client-player-to-game-that-was-created-by-ot
 * @author Maria Legaspi
 *
 */

public class Server{
	// port as all clients should be able to access this port
	public static int portNumber = 4000;
	// server socket
	private ServerSocket listener;
	// client sockets
	private Set<Socket> sockets = new HashSet<Socket>();
	// client threads associated with sockets
	private Set<ClientThread> threads = new HashSet<ClientThread>();
	// commands received from clients
	private Queue<String> commands = new LinkedList<String>();

	/**
	 * Accepts clients wanting to connect to the server.
	 * @throws Exception
	 */
    public void runServer() throws IOException {
        listener = new ServerSocket(portNumber);
        try{
            while(true){
				// accept a new client
				Socket socket = listener.accept();
				sockets.add(socket);

				// make a thread associated with the socket
				ClientThread clientThread = new ClientThread(this, socket);
				threads.add(clientThread);
				clientThread.start();

//                BufferedReader readerChannel = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                BufferedWriter writerChannel = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//                try {
//                    writerChannel.write("Socket connected\n"); // writes to a stream
//                    writerChannel.flush(); // empties the stream
//
//                    while ((line = readerChannel.readLine()) != null) {
//                        System.out.println(line);
//                    }
//                }
//                finally {
//                    socket.close();
//                }

            }
        }
        catch(IOException e){
        	System.out.println("Failed to accept client on port " + portNumber + "\n");
        }
        finally {
            listener.close();
        }
    }

    /**
     * Updates all clients
     * @param message
     */
    public void updateAll(String message){
    	try{
          for(Socket sock: sockets){
        	  BufferedWriter writerChannel = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
        	  writerChannel.write(message); // sends message to client
        	  writerChannel.flush(); // empties the stream
          }
    	}
    	catch(IOException e){
    		System.out.println("Failed to create buffered writer.\n");
    	}
    }
}
