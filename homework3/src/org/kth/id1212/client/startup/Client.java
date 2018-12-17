package org.kth.id1212.client.startup;

import org.kth.id1212.client.view.CLIView;
import org.kth.id1212.common.FileCatalog;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
  public static void main(String[] args) {

    try {
      FileCatalog fileCatalog = (FileCatalog) Naming.lookup(FileCatalog.REGISTRY_NAME);
      new CLIView(fileCatalog).start();
    } catch (NotBoundException | MalformedURLException | RemoteException e) {
      e.printStackTrace();
    }
  }
}
