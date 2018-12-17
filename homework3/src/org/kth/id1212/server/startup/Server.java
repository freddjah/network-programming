package org.kth.id1212.server.startup;

import org.kth.id1212.common.FileCatalog;
import org.kth.id1212.server.controller.Controller;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Server {

  public static void main(String[] args) {

    try {
      Server server = new Server();
      server.startRMIServant();
      System.out.println("Server started...");

    } catch(RemoteException | SQLException | ClassNotFoundException | MalformedURLException e) {
      e.printStackTrace();
    }
  }

  private void startRMIServant() throws RemoteException, SQLException, ClassNotFoundException, MalformedURLException {

    try {
      LocateRegistry.getRegistry().list();
    } catch (RemoteException e) {
      LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
    }

    Connection connection = this.connectToDatabase();
    Controller controller = new Controller(connection);
    Naming.rebind(FileCatalog.REGISTRY_NAME, controller);
  }

  private Connection connectToDatabase() throws ClassNotFoundException, SQLException {

    Class.forName("com.mysql.jdbc.Driver");

    return DriverManager.getConnection("jdbc:mysql://localhost:3306/filecatalog", "root", "homework3");
  }
}
