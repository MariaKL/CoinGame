package kaukau.control;

import java.net.Socket;

/**
 * Thread for concurrency.
 * @author Maria Legaspi
 *
 */

//TODO: use thread

public class ClientThread extends Thread{
	private Server server;
	private Socket clientSock;

	public ClientThread(Server server, Socket clientSock){
		this.server = server;
		this.clientSock = clientSock;
	}
}
