package org.kth.id1212;

import java.io.*;
import java.net.*;

class Client {
  public static void main(String args[]) throws Exception {

    BufferedReader inputFromUser = new BufferedReader(new InputStreamReader(System.in));
    Socket clientSocket = new Socket("localhost", 3000);
    DataOutputStream outputToServer = new DataOutputStream(clientSocket.getOutputStream());
    BufferedReader inputFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

    System.out.println("A connection to server is established. Enter command below");

    while (true) {

      String command = inputFromUser.readLine();
      outputToServer.writeBytes(command + "\n");
      String response = inputFromServer.readLine();
      System.out.println(response);
    }
  }
}