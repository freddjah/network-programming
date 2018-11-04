package org.kth.id1212.client.startup;

import org.kth.id1212.client.controller.ClientController;

/**
 * GameClient
 */
public class Client {

  public static void main(String args[]) {

    try {
      new ClientController("localhost", 3000);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}