package org.kth.id1212.client.net;

import org.kth.id1212.client.controller.GameController;
import org.kth.id1212.common.Command;

/**
 * This is the class
 */
public class ServerOutputThread extends Thread {

  GameController controller;

  public ServerOutputThread(GameController controller) {
    this.controller = controller;
  }

  @Override
  public void run() {
    while (true) {
      try {
        Command serverResponse = this.controller.receiveServerResponse();
        this.controller.handleServerResponse(serverResponse);
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Something went wrong. Shutting down.");
        System.exit(-1);
      }
    }
  }
}
