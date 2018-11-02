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
      SessionHandler handler = new SessionHandler(clientSocket);
      handler.start();
    }
  }
}