package org.kth.id1212;

import java.io.*;
import java.net.*;

public class Server {

  public static final int SERVER_PORT = 3000;

  public static void main(String[] args) throws Exception {
    
    ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
    System.out.println("Listening on port " + SERVER_PORT);

    while (true) {

      Socket clientSocket = serverSocket.accept();
      BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      DataOutputStream clientWriter = new DataOutputStream(clientSocket.getOutputStream());

      String command = clientReader.readLine();
      System.out.println("Received command from client: " + command);

      // type=guess;

      clientWriter.writeBytes("I hear you: " + command.toUpperCase() + "\n");
    }

    
  }
}