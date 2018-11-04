package org.kth.id1212.server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {

  public void start(int port) throws IOException {
    
    ServerSocket serverSocket = new ServerSocket(port);
    System.out.println("Listening on port " + port);

    while (true) {

      Socket clientSocket = serverSocket.accept();
      SessionHandler handler = new SessionHandler(clientSocket);
      handler.start();
    }
  }
}