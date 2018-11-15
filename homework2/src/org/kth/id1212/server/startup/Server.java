package org.kth.id1212.server.startup;

import org.kth.id1212.server.net.GameServer;

public class Server {

  public static final int SERVER_PORT = 3000;

  public static void main(String[] args) {

    try {
      GameServer server = new GameServer();
      server.start(SERVER_PORT);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
