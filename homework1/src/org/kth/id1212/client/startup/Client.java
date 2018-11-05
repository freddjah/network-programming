package org.kth.id1212.client.startup;

import java.io.IOException;
import java.net.ConnectException;

import org.kth.id1212.client.controller.ClientController;

/**
 * GameClient
 */
public class Client {

  public static void main(String args[]) {

    try {
      new ClientController("localhost", 3000);
    } catch (ConnectException e) {
      System.out.println("Server is unavailable.");
    } catch (IOException e) {
      System.out.println("Something went wrong while connecting to the server.");
    }
  }
}