/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.example.utilities.ClientMessage;
import org.example.utilities.StandardInputUtil;

public class Client {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println(
                "Usage: java Client <host name> <port number>");
            System.exit(1);
        }
 
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
 
        try {
            Socket serverSocket = new Socket(hostName, portNumber);
            ServerStub server = new ServerStub(serverSocket);

            ListenerThread listener = new ListenerThread(server);
            listener.start();

            String userInput;
            String[] userInputArray;
            while (!(userInput = StandardInputUtil.readLine()).toLowerCase().equals("quit")) {
                userInputArray = userInput.split(" ", 2);
                server.sendMessage(userInputArray[0], (userInputArray.length > 1 ? userInputArray[1] : null));
            }

            //stop listener
            listener.stop();

            //send "quit" to server
            server.sendMessage(new ClientMessage(ClientMessage.Type.QUIT, null));
            server.close();

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        } 
    }
}
