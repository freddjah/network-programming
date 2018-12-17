package org.kth.id1212.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {

  public void sendMessage(String message) throws RemoteException;
}
