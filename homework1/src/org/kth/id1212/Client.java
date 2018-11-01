package org.kth.id1212;

import java.io.*;
import java.net.*;

class Client {
  public static void main(String args[]) throws Exception {
    String sentence;
    String modifiedSentence;

    BufferedReader inputFromUser = new BufferedReader(new InputStreamReader(System.in));
    Socket clientSocket = new Socket("localhost", 3000);
    DataOutputStream outputToServer = new DataOutputStream(clientSocket.getOutputStream());
    BufferedReader inputFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


    sentence = inputFromUser.readLine();

    outputToServer.writeBytes(sentence + "\n");
    modifiedSentence = inputFromServer.readLine();
    System.out.println("FROM SERVER: " + modifiedSentence);
    clientSocket.close();
  }
}