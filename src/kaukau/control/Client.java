package kaukau.control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

/**
 * Based off http://stackoverflow.com/questions/29545597/multiplayer-game-in-java-connect-client-player-to-game-that-was-created-by-ot
 * @author Maria Legaspi
 *
 */

public class Client{
    public static void main(String[] args) throws Exception  {
        Socket socket = new Socket("127.0.0.1", Server.portNumber);
        BufferedWriter writerChannel = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader readerChannel = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line;

        writerChannel.write("New client\n");
        writerChannel.flush();

        while ((line = readerChannel.readLine()) != null)  {
            System.out.println(line);
        }
    }
}