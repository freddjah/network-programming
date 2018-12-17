package org.kth.id1212.client.view;

import org.kth.id1212.common.Client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MessageClient extends UnicastRemoteObject implements Client {

  public MessageClient() throws RemoteException {
    super();
  }

  @Override
  public void sendMessage(String message) {
    System.out.println("\nGot message: " + message);
  }
}
